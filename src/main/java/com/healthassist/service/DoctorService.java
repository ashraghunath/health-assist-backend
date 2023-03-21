package com.healthassist.service;

import com.healthassist.common.PatientRecordStatus;
import com.healthassist.common.UserCommonService;
import com.healthassist.entity.AssignedPatient;
import com.healthassist.entity.DoctorAppointment;
import com.healthassist.entity.PatientRecord;
import com.healthassist.entity.User;
import com.healthassist.exception.AlreadyExistsException;
import com.healthassist.exception.InvalidUserRequestException;
import com.healthassist.exception.ResourceNotFoundException;
import com.healthassist.mapper.AppointmentMapper;
import com.healthassist.mapper.AssignedPatientMapper;
import com.healthassist.mapper.UserMapper;
import com.healthassist.repository.AssignedPatientRepository;
import com.healthassist.repository.DoctorAppointmentRepository;
import com.healthassist.repository.PatientRecordRepository;
import com.healthassist.repository.UserRepository;
import com.healthassist.request.AppointmentListForDateRequest;
import com.healthassist.request.AppointmentRequest;
import com.healthassist.response.AppointmentListForDateResponse;
import com.healthassist.response.AppointmentResponse;
import com.healthassist.response.AssignedPatientResponse;
import com.healthassist.response.PatientRecordResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DoctorService {
    @Autowired
    UserCommonService userCommonService;
    @Autowired
    AssignedPatientMapper assignedPatientMapper;
    @Autowired
    AssignedPatientRepository assignedPatientRepository;
    @Autowired
    PatientRecordRepository patientRecordRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    DoctorAppointmentRepository appointmentRepository;

    @Autowired
    AppointmentMapper appointmentMapper;

    @Autowired
    UserMapper userMapper;
    @Autowired
    PatientService patientService;
    @Autowired
    PatientRecordService patientRecordService;

    public Page<AssignedPatientResponse> getAssignedPatients(Pageable pageable) {
        String userRegisterNumber = userCommonService.getUser().getRegistrationNumber();

        Page<AssignedPatient> assignedPatientPage = assignedPatientRepository.findByDoctorRegistrationNumberOrderByCreatedAtDesc(userRegisterNumber, pageable);

        return assignedPatientPage.map(assignedPatientMapper::toAssignedPatientResponse);
    }

    public PatientRecordResponse getActivePatient(String patientRecordId) {
        PatientRecord patientRecord = patientRecordRepository.findByPatientRecordId(patientRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        PatientRecordResponse response = new PatientRecordResponse();
        response.setPatient(
                userMapper.toUserResponse(userRepository.findByUserIdAndDeletedFalse(patientRecord.getPatientId())));
        response.setRecordId(patientRecordId);
        response.setCreatedAt(patientRecord.getCreatedAt());
        response.setAssessmentResult(patientService.getAssessmentResult(patientRecord.getAssessmentResultId()));
        return response;
    }

    public Page<AppointmentResponse> getDoctorAppointments(Pageable pageable) {
        User user = userCommonService.getUser();

        Page<DoctorAppointment> pages = appointmentRepository.findByDoctorIdAndStartDateTimeGreaterThanEqualOrderByCreatedAtDesc(user.getUserId(), LocalDateTime.now(), pageable);

        return pages.map(appointmentMapper::toAppointmentResponse);
    }

    public List<AppointmentListForDateResponse> getDoctorAppointmentsByDate(AppointmentListForDateRequest request) {
        if (request.getDate() == null) {
            throw new InvalidUserRequestException("date cannot be null");
        }
        User user = userCommonService.getUser();

        return appointmentRepository.findByDoctorIdAndStartDateTimeBetweenOrderByCreatedAtDesc(user.getUserId(), request.getDate(), request.getDate().plusDays(1));
    }

    public void storeDoctorAppointment(AppointmentRequest appointmentRequest) {
        String doctorId = userCommonService.getUser().getUserId();
        LocalDateTime nowZonedDateTime = LocalDateTime.now();
        if (appointmentRequest.getStartDateTime().isBefore(nowZonedDateTime) ||
                appointmentRequest.getStartDateTime().isEqual(nowZonedDateTime) ||
                appointmentRequest.getStartDateTime().isAfter(appointmentRequest.getEndDateTime()) ||
                appointmentRequest.getStartDateTime().isEqual(appointmentRequest.getEndDateTime())) {
            throw new InvalidUserRequestException("appointment time period invalid");
        }
        if (!patientRecordRepository.existsByPatientRecordId(appointmentRequest.getPatientRecordId())) {
            throw new ResourceNotFoundException(String.format("patient record %s not found", appointmentRequest.getPatientRecordId()));
        }
        if (appointmentRepository.existsByPatientRecordId(appointmentRequest.getPatientRecordId())) {
            throw new AlreadyExistsException("patient already has reserved timeslot");
        }
        if (appointmentRepository.existsByDoctorIdAndStartDateTimeBetweenOrStartDateTimeEquals(
                doctorId,
                appointmentRequest.getStartDateTime(), appointmentRequest.getEndDateTime(),
                appointmentRequest.getStartDateTime()) ||
                appointmentRepository.existsByDoctorIdAndEndDateTimeBetweenOrEndDateTimeEquals(
                        doctorId,
                        appointmentRequest.getStartDateTime(), appointmentRequest.getEndDateTime(),
                        appointmentRequest.getEndDateTime())) {
            throw new AlreadyExistsException("conflict: doctor has the reserved time slot during the provided time period");
        }
        PatientRecord patientRecord = patientRecordRepository.findByPatientRecordId(appointmentRequest.getPatientRecordId()).orElseThrow(() -> new ResourceNotFoundException("Patient Record Not Found"));
        ;
        if (patientRecord.getStatus() != PatientRecordStatus.DOCTOR_IN_PROGRESS) {
            throw new ResourceNotFoundException(String.format("patient record %s not found", appointmentRequest.getPatientRecordId()));
        }
        // save doctor appointment
        DoctorAppointment doctorAppointment = appointmentMapper.fromAppointmentRequestToDoctorAppointment(appointmentRequest);
        doctorAppointment = appointmentRepository.save(doctorAppointment);

        // update patient record
        patientRecordService.afterAppointment(doctorAppointment, patientRecord, PatientRecordStatus.DOCTOR_APPOINTMENT);
    }

    public void rejectAssignedPatient(String patientRecordId) {
        PatientRecord patientRecord = patientRecordRepository.findByPatientRecordId(patientRecordId).orElseThrow(() -> new ResourceNotFoundException("Patient record Not found"));
        if (patientRecord != null &&
                (patientRecord.getStatus() == PatientRecordStatus.DOCTOR_IN_PROGRESS || patientRecord.getStatus() == PatientRecordStatus.DOCTOR_APPOINTMENT)) {
            assignedPatientRepository.deleteById(patientRecord.getAssignedPatientId());

            if (patientRecord.getAppointmentId() != null &&
                    patientRecord.getStatus() == PatientRecordStatus.DOCTOR_APPOINTMENT) {
                // delete doctor appointment
                appointmentRepository.deleteByAppointmentId(patientRecord.getAppointmentId());
            }

            patientRecordService.afterRejectingPatient(patientRecord, PatientRecordStatus.DOCTOR_REJECTED);
            return;
        }
        throw new ResourceNotFoundException("patient record not found");
    }
    public void deleteAppointment(String appointmentId){
        DoctorAppointment appointmentDetails=appointmentRepository.findByAppointmentId(appointmentId);
        appointmentRepository.deleteByAppointmentId(appointmentId);
        PatientRecord record=patientRecordRepository.findByPatientRecordId(appointmentDetails.getPatientRecordId()).orElseThrow(()->new ResourceNotFoundException("Patient record not found"));
        patientRecordRepository.deletePatientRecordByPatientRecordId(appointmentDetails.getPatientRecordId());
        record.setStatus(PatientRecordStatus.DOCTOR_IN_PROGRESS);
        patientRecordRepository.save(record);
    }
}
