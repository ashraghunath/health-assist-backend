package com.healthassist.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.healthassist.common.PatientRecordStatus;
import com.healthassist.entity.Assessment;
import com.healthassist.entity.DoctorAppointment;
import com.healthassist.entity.PatientRecord;
import com.healthassist.exception.ResourceNotFoundException;
import com.healthassist.repository.*;
import com.healthassist.request.UserRequest;
import com.healthassist.response.*;
import com.healthassist.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthassist.entity.User;
import com.healthassist.mapper.UserMapper;
import com.healthassist.common.AuthorityName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class AdminService {

	@Autowired
	private AssessmentRepository assessmentRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserMapper userMapper;

	@Autowired
	ActivePatientRepository activePatientRepository;

	@Autowired
	CounselorAppointmentRepository counselorAppointmentRepository;

	@Autowired
	DoctorAppointmentRepository doctorAppointmentRepository;

	@Autowired
	AssignedPatientRepository assignedPatientRepository;

	@Autowired
	private PatientRecordRepository patientRecordRepository;

	@Autowired
	private BaseService baseService;

	@Autowired
	private PatientRecordService patientRecordService;

	public void createAssessment(Assessment assessment) {
		assessmentRepository.save(assessment);
	}

	public AdminPatientReport getAdminPatientReportByRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
		AdminPatientReport report = new AdminPatientReport();
		List<User> patientCardPage = userRepository.findByAuthorityContainsAndCreatedAtBetweenAndDeletedFalseOrderByCreatedAt
				(AuthorityName.ROLE_PATIENT,
						startDateTime,
						endDateTime);
		System.out.println(patientCardPage.toString());
		report.setPatients(patientCardPage.stream().map(userMapper::toAdminPatientCard).collect(Collectors.toList()));
		report.setNumAttemptedAssessment(activePatientRepository.countByCreatedAtBetween(startDateTime, endDateTime));
		report.setNumTotal(patientCardPage.size());
		report.setNumHasCounselorAppointment(counselorAppointmentRepository.countByStartDateTimeBetween(startDateTime, endDateTime));
		Integer numHasDoctorAppointment = doctorAppointmentRepository.countByStartDateTimeBetween(startDateTime, endDateTime);
		report.setNumHasDoctorAppointment(numHasDoctorAppointment);
		report.setNumInProcessingDoctorAppointment(assignedPatientRepository.countByCreatedAtBetween(startDateTime, endDateTime) - numHasDoctorAppointment);
		return report;
	}

	public AdminPatientReportParameters getAdminPatientReportParameters() {
		AdminPatientReportParameters report = new AdminPatientReportParameters();
		Integer totalUsers = userRepository.countByAuthorityContains(AuthorityName.ROLE_PATIENT);
		report.setNumAttemptedAssessment(activePatientRepository.countBy());
		report.setNumTotal(totalUsers);
		report.setNumHasCounselorAppointment(counselorAppointmentRepository.countBy());
		Integer numHasDoctorAppointment = doctorAppointmentRepository.countBy();
		report.setNumHasDoctorAppointment(numHasDoctorAppointment);
		report.setNumInProcessingDoctorAppointment(assignedPatientRepository.countBy() - numHasDoctorAppointment);
		return report;
	}

	public Page<AdminPatientCard> getPatients(Pageable pageable) {
		Page<User> patientCardPage = userRepository.findByAuthorityContainsAndDeletedFalseOrderByCreatedAtDesc(AuthorityName.ROLE_PATIENT, pageable);
		return patientCardPage.map(userMapper::toAdminPatientCard);
	}

	public Page<AdminDoctorCard> getDoctors(Pageable pageable) {
		Page<User> patientCardPage = userRepository.findByAuthorityContainsAndDeletedFalseOrderByCreatedAtDesc(AuthorityName.ROLE_DOCTOR, pageable);
		return patientCardPage.map(userMapper::toAdminDoctorCard);
	}

	public AdminUserCreateResponse createPatient(UserRequest userRequest) {
		return createUser(userRequest, AuthorityName.ROLE_PATIENT);
	}

	public AdminUserCreateResponse createDoctor(UserRequest userRequest) {
		return createUser(userRequest, AuthorityName.ROLE_DOCTOR);
	}

	private AdminUserCreateResponse createUser(UserRequest userRequest, AuthorityName authorityName) {
		userRequest.setPassword(UserUtil.generateRandomPassword());
		System.out.println("Password: " + userRequest.getPassword());
		LoginResponse loginResponse = baseService.signUp(userRequest, authorityName, true);
		AdminUserCreateResponse response = new AdminUserCreateResponse();
		response.setSuccess(loginResponse.isLoginSuccess());
		response.setErrorMessage(loginResponse.getErrorMessage());
		response.setUser(loginResponse.getUser());
		return response;
	}

	public void removeDoctor(String emailAddress) {
		removeUser(emailAddress, AuthorityName.ROLE_DOCTOR);
	}

	private void removeUser(String emailAddress, AuthorityName authorityName) {
		User user = userRepository.findByEmailAddressAndAuthorityContainsAndDeletedFalse(emailAddress, Collections.singleton(authorityName));
		System.out.println("hola" + user);
/*		if (authorityName == AuthorityName.ROLE_PATIENT) {
			activePatientRepository.deleteByPatientId(user.getUserId());
			assessmentResultRepository.deleteByPatientId(user.getUserId());
			assignedPatientRepository.deleteByPatientId(user.getUserId());
			counselorAppointmentRepository.deleteByPatientId(user.getUserId());
			doctorAppointmentRepository.deleteByPatientId(user.getUserId());
			patientRecordRepository.deleteByPatientId(user.getUserId());
		}
		if (authorityName == AuthorityName.ROLE_COUNSELOR) {
			List<CounselorAppointment> appointments = counselorAppointmentRepository.findByCounselorId(user.getUserId());
			for (CounselorAppointment appointment : appointments) {
				List<PatientRecord> patientRecords = patientRecordRepository.findByAppointmentIdAndStatus(appointment.getAppointmentId(), PatientRecordStatus.COUNSELOR_APPOINTMENT);
				patientRecords.forEach(patientRecord -> {
					if (patientRecord.getActivePatientId() != null)
						activePatientRepository.deleteByActivePatientId(patientRecord.getActivePatientId());
					patientRecordRepository.deleteByPatientRecordId(patientRecord.getPatientRecordId());
				});
				patientRecords = patientRecordRepository.findByAppointmentIdAndStatus(appointment.getAppointmentId(), PatientRecordStatus.COUNSELOR_IN_PROGRESS);
				patientRecords.forEach(patientRecord -> {
					if (patientRecord.getActivePatientId() != null)
						activePatientRepository.deleteByActivePatientId(patientRecord.getActivePatientId());
					patientRecordRepository.deleteByPatientRecordId(patientRecord.getPatientRecordId());
				});
				counselorAppointmentRepository.deleteByAppointmentId(appointment.getAppointmentId());
			}
		}*/
		if (authorityName == AuthorityName.ROLE_DOCTOR) {
			List<DoctorAppointment> appointments = doctorAppointmentRepository.findByDoctorId(user.getUserId());
			for (DoctorAppointment appointment : appointments) {
				List<PatientRecord> patientRecords = patientRecordRepository.findByAppointmentIdAndStatus(appointment.getAppointmentId(), PatientRecordStatus.DOCTOR_APPOINTMENT);
				patientRecords.forEach(patientRecord -> {
					if (patientRecord.getActivePatientId() != null)
						activePatientRepository.deleteByActivePatientId(patientRecord.getActivePatientId());
					patientRecordRepository.deleteByPatientRecordId(patientRecord.getPatientRecordId());
				});
				patientRecords = patientRecordRepository.findByAppointmentIdAndStatus(appointment.getAppointmentId(), PatientRecordStatus.DOCTOR_IN_PROGRESS);
				patientRecords.forEach(patientRecord -> {
					if (patientRecord.getActivePatientId() != null)
						activePatientRepository.deleteByActivePatientId(patientRecord.getActivePatientId());
					patientRecordRepository.deleteByPatientRecordId(patientRecord.getPatientRecordId());
				});
				doctorAppointmentRepository.deleteByAppointmentId(appointment.getAppointmentId());
			}
			assignedPatientRepository.deleteByDoctorRegistrationNumber(user.getRegistrationNumber());
		}
		if (user != null) {
			user.setDeleted(true);
			userRepository.save(user);
		} else {
			throw new ResourceNotFoundException("user not found!");
		}
	}

	public void resetUsers() {
		userRepository.findAll().forEach(user -> {
			user.setDeleted(false);
			user.setPasswordAutoGenerated(false);
			userRepository.save(user);
		});
	}


}
