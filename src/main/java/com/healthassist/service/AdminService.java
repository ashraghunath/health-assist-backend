package com.healthassist.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.healthassist.request.UserRequest;
import com.healthassist.response.*;
import com.healthassist.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthassist.entity.User;
import com.healthassist.mapper.UserMapper;
import com.healthassist.repository.ActivePatientRepository;
import com.healthassist.repository.AssignedPatientRepository;
import com.healthassist.repository.CounselorAppointmentRepository;
import com.healthassist.repository.DoctorAppointmentRepository;
import com.healthassist.repository.UserRepository;
import com.healthassist.common.AuthorityName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class AdminService {

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
	private BaseService baseService;

	@Autowired
	private PatientRecordService patientRecordService;

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

	public AdminUserCreateResponse createPatient(UserRequest userRequest) {
		return createUser(userRequest, AuthorityName.ROLE_PATIENT);
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

}
