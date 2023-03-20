package com.healthassist.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import javax.validation.Valid;

import com.healthassist.common.AuthorityName;
import com.healthassist.entity.AssignedPatient;
import com.healthassist.repository.*;
import com.healthassist.request.DoctorAssignmentRequest;
import com.healthassist.response.CounselorDoctorCardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.healthassist.common.PatientRecordStatus;
import com.healthassist.common.UserCommonService;
import com.healthassist.entity.CounselorAppointment;
import com.healthassist.entity.PatientRecord;
import com.healthassist.entity.User;
import com.healthassist.exception.AlreadyExistsException;
import com.healthassist.exception.InvalidAppointmentRequestException;
import com.healthassist.exception.ResourceNotFoundException;
import com.healthassist.response.AppointmentListForDateResponse;
import com.healthassist.response.AppointmentResponse;
import com.healthassist.request.AppointmentListForDateRequest;
import com.healthassist.response.PatientRecordResponse;
import com.healthassist.mapper.AppointmentMapper;
import com.healthassist.mapper.UserMapper;
import com.healthassist.request.AppointmentRequest;

@Service
public class CounselorService {

	@Autowired
	PatientRecordRepository patientRecordRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserMapper userMapper;

	@Autowired
	PatientService patientService;

	@Autowired
	PatientRecordService patientRecordService;

	@Autowired
	UserCommonService userCommonService;

	@Autowired
	CounselorAppointmentRepository counselorAppointmentRepository;

	@Autowired
	AppointmentMapper appointmentMapper;

	@Autowired
	AssignedPatientRepository assignedPatientRepository;

	@Autowired
	ActivePatientRepository activePatientRepository;

	public PatientRecordResponse getActivePatient(String patientRecordId) {
		PatientRecord patientRecord = patientRecordRepository.findByPatientRecordId(patientRecordId)
				.orElseThrow(() -> new ResourceNotFoundException("Patient not found in Active Patient Records"));
		PatientRecordResponse response = new PatientRecordResponse();
		response.setPatient(
				userMapper.toUserResponse(userRepository.findByUserIdAndDeletedFalse(patientRecord.getPatientId())));
		response.setRecordId(patientRecord.getPatientRecordId());
		response.setCreatedAt(patientRecord.getCreatedAt());
		response.setAssessmentResult(patientService.getAssessmentResult(patientRecord.getAssessmentResultId()));
		return response;
	}

	public void storeCounselorAppointment(@Valid AppointmentRequest appointmentRequest) {
		// TODO Auto-generated method stub
		String counselorId = userCommonService.getUser().getUserId();
		LocalDateTime currentDateTime = LocalDateTime.now();
		if (appointmentRequest.getStartDateTime().isBefore(currentDateTime)
				|| appointmentRequest.getStartDateTime().isEqual(currentDateTime)
				|| appointmentRequest.getStartDateTime().isAfter(appointmentRequest.getEndDateTime())
				|| appointmentRequest.getStartDateTime().isEqual(appointmentRequest.getEndDateTime())) {

			throw new InvalidAppointmentRequestException(
					"Invalid values entered!! Select appropriate date and time to make an appointment");
		}

		if (!patientRecordRepository.existsByPatientRecordId(appointmentRequest.getPatientRecordId())) {
			throw new ResourceNotFoundException(
					String.format("patient record %s not found", appointmentRequest.getPatientRecordId()));
		}

		if (counselorAppointmentRepository.existsByPatientRecordId(appointmentRequest.getPatientRecordId())) {
			throw new AlreadyExistsException("The Timeslot is Reserved. Please select again!");
		}

		if (counselorAppointmentRepository.existsByCounselorIdAndStartDateTimeBetweenOrStartDateTimeEquals(counselorId,
				appointmentRequest.getStartDateTime(), appointmentRequest.getEndDateTime(),
				appointmentRequest.getStartDateTime())
				|| counselorAppointmentRepository.existsByCounselorIdAndEndDateTimeBetweenOrEndDateTimeEquals(
						counselorId, appointmentRequest.getStartDateTime(), appointmentRequest.getEndDateTime(),
						appointmentRequest.getEndDateTime())) {
			throw new AlreadyExistsException("There is a Conflict!! Already Reserved TimeSlot.");
		}

		PatientRecord patientRecord = patientRecordRepository
				.findByPatientRecordId(appointmentRequest.getPatientRecordId())
				.orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
		if (patientRecord.getStatus() != PatientRecordStatus.COUNSELOR_IN_PROGRESS) {
			throw new ResourceNotFoundException(String.format("Patient with %s has already been booked!!",
					appointmentRequest.getPatientRecordId()));
		}

		CounselorAppointment counselorAppointment = appointmentMapper
				.fromAppointmentRequestToCounselorAppointment(appointmentRequest);
		counselorAppointment = counselorAppointmentRepository.save(counselorAppointment);

		patientRecordService.afterAppointment(counselorAppointment, patientRecord,
				PatientRecordStatus.COUNSELOR_APPOINTMENT);

	}

	public List<AppointmentListForDateResponse> getCounselorAppointmentsByDate(@Valid AppointmentListForDateRequest requestDate) {
		if (requestDate.getDate() == null) {
			throw new InvalidAppointmentRequestException("Date cannot be null");
		}
		User user = userCommonService.getUser();

		return counselorAppointmentRepository.findByCounselorIdAndStartDateTimeBetweenOrderByCreatedAtDesc(
				user.getUserId(), requestDate.getDate(), requestDate.getDate().plusDays(1));
	}
	
	public Page<AppointmentResponse> getCounselorAppointments(Pageable pageable) {
		User user = userCommonService.getUser();

		Page<CounselorAppointment> pages = counselorAppointmentRepository
				.findByCounselorIdAndStartDateTimeGreaterThanEqualOrderByCreatedAtDesc(user.getUserId(),
							LocalDateTime.now(ZoneOffset.UTC), pageable);

		return pages.map(appointmentMapper::toAppointmentResponse);
	}

	public void cancelAppointment(String appointmentId) {
		String patientRecordId = counselorAppointmentRepository.findByAppointmentId(appointmentId).getPatientRecordId();
		PatientRecord patientRecord = patientRecordRepository
				.findByPatientRecordId(patientRecordId).orElseThrow(()->new ResourceNotFoundException("Patient Record Does not exist"));
		if (patientRecord.getStatus() == PatientRecordStatus.COUNSELOR_APPOINTMENT
				&& patientRecord.getAppointmentId() != null) {
			counselorAppointmentRepository.deleteByAppointmentId(patientRecord.getAppointmentId());
		}		
		patientRecord.update();
		patientRecord.setAppointmentId(null);
		patientRecord.setStatus(PatientRecordStatus.COUNSELOR_IN_PROGRESS);
		patientRecordRepository.save(patientRecord);
	}

	public Page<CounselorDoctorCardResponse> getDoctorPage(Pageable pageable) {
		Page<User> page = userRepository.findByAuthorityContainsAndDeletedFalseOrderByCreatedAtDesc(AuthorityName.ROLE_DOCTOR, pageable);

		return page.map(userMapper::toCounselorDoctorCardResponse);
	}

	public void assignDoctorToPatient(DoctorAssignmentRequest doctorAssignmentRequest) {
		String counselorRegistrationNumber = userCommonService.getUser().getRegistrationNumber();
		if (!patientRecordRepository.existsByPatientRecordId(doctorAssignmentRequest.getActivePatientId())) {
			throw new ResourceNotFoundException(String.format("patient record %s not found", doctorAssignmentRequest.getActivePatientId()));
		}
		if (!userRepository.existsByRegistrationNumberAndDeletedFalse(doctorAssignmentRequest.getDoctorRegistrationNumber())) {
			throw new ResourceNotFoundException(String.format("doctor with %s not found", doctorAssignmentRequest.getDoctorRegistrationNumber()));
		}
		// check if the patient record has already been assigned to a doctor
		if (assignedPatientRepository.existsByPatientRecordId(doctorAssignmentRequest.getActivePatientId())) {
			throw new AlreadyExistsException(String.format("patient record %s is already assigned to a doctor", doctorAssignmentRequest.getActivePatientId()));
		}

		PatientRecord patientRecord = patientRecordRepository.findByPatientRecordId(doctorAssignmentRequest.getActivePatientId()).orElseThrow(() -> new ResourceNotFoundException("Patient not found"));;
		if (patientRecord.getStatus() != PatientRecordStatus.COUNSELOR_IN_PROGRESS &&
				patientRecord.getStatus() != PatientRecordStatus.COUNSELOR_APPOINTMENT) {
			throw new ResourceNotFoundException(String.format("patient record %s not found", doctorAssignmentRequest.getActivePatientId()));
		}

		// before forwarding to a doctor, delete any existing appointment with the counselor.
		if (patientRecord.getStatus() == PatientRecordStatus.COUNSELOR_APPOINTMENT &&
				patientRecord.getAppointmentId() != null) {
			counselorAppointmentRepository.deleteByAppointmentId(patientRecord.getAppointmentId());
		}

		// save assigned patient record
		AssignedPatient assignedPatient = new AssignedPatient();
		assignedPatient.setPatientRecordId(patientRecord.getPatientRecordId());
		assignedPatient.setDoctorRegistrationNumber(doctorAssignmentRequest.getDoctorRegistrationNumber());
		assignedPatient.setCounselorRegistrationNumber(counselorRegistrationNumber);
		assignedPatient.setPatientId(patientRecord.getPatientId());
		assignedPatient = assignedPatientRepository.save(assignedPatient);

		// update patient record after assigning a doctor to active patient record
		patientRecordService.afterAssigningDoctor(assignedPatient, patientRecord);
	}

	public void rejectPatient(String patientRecordId) {
		PatientRecord patientRecord = patientRecordRepository.findByPatientRecordId(patientRecordId).orElseThrow(() -> new ResourceNotFoundException("Patient record Not found"));
		if (patientRecord != null &&(patientRecord.getStatus() == PatientRecordStatus.COUNSELOR_IN_PROGRESS || patientRecord.getStatus() == PatientRecordStatus.COUNSELOR_APPOINTMENT)) {
			activePatientRepository.deleteByActivePatientId(patientRecord.getActivePatientId());

			if (patientRecord.getAppointmentId() != null &&
					patientRecord.getStatus() == PatientRecordStatus.COUNSELOR_APPOINTMENT) {
				// delete counselor appointment
				counselorAppointmentRepository.deleteByAppointmentId(patientRecord.getAppointmentId());
			}
			patientRecordService.afterRejectingPatient(patientRecord, PatientRecordStatus.COUNSELOR_REJECTED);
			return;
		}
		throw new ResourceNotFoundException("Patient Record Not Found");
	}
		public void editAppointment(@Valid AppointmentRequest appointmentRequest) {
		
		String counselorId = userCommonService.getUser().getUserId();
		if (!patientRecordRepository.existsByPatientRecordId(appointmentRequest.getPatientRecordId())) {
			throw new ResourceNotFoundException(
					String.format("patient record %s not found", appointmentRequest.getPatientRecordId()));
		}
		
		if (counselorAppointmentRepository.existsByCounselorIdAndStartDateTimeBetweenOrStartDateTimeEquals(counselorId,
				appointmentRequest.getStartDateTime(), appointmentRequest.getEndDateTime(),
				appointmentRequest.getStartDateTime())
				|| counselorAppointmentRepository.existsByCounselorIdAndEndDateTimeBetweenOrEndDateTimeEquals(
						counselorId, appointmentRequest.getStartDateTime(), appointmentRequest.getEndDateTime(),
						appointmentRequest.getEndDateTime())) {
			throw new AlreadyExistsException("There is a Conflict!! Already Reserved TimeSlot.");
		}
		
		PatientRecord patientRecord = patientRecordRepository
				.findByPatientRecordId(appointmentRequest.getPatientRecordId())
				.orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
		CounselorAppointment existingAppointment = counselorAppointmentRepository.findByAppointmentId(patientRecord.getAppointmentId());
		if(existingAppointment== null) {
			throw new InvalidAppointmentRequestException("No Appoint exists for this patient. Create one first!!");
		}
		existingAppointment.update();
		existingAppointment.setStartDateTime(appointmentRequest.getStartDateTime());
		existingAppointment.setEndDateTime(appointmentRequest.getEndDateTime());
		counselorAppointmentRepository.save(existingAppointment);
		
	}

}
