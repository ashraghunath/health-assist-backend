package com.healthassist.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import javax.validation.Valid;
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
import com.healthassist.repository.UserRepository;
import com.healthassist.request.AppointmentRequest;
import com.healthassist.repository.CounselorAppointmentRepository;
import com.healthassist.repository.PatientRecordRepository;

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

}
