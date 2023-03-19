package com.healthassist.mapper;

import com.healthassist.common.UserCommonService;
import com.healthassist.entity.*;
import com.healthassist.exception.ResourceNotFoundException;
import com.healthassist.repository.PatientRecordRepository;
import com.healthassist.repository.UserRepository;
import com.healthassist.request.AppointmentRequest;
import com.healthassist.response.AppointmentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;


@Component
public class AppointmentMapper {

    @Autowired
    private UserCommonService userCommonService;

    @Autowired
    PatientRecordRepository patientRecordRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRepository userRepository;

    public CounselorAppointment fromAppointmentRequestToCounselorAppointment(
            @Valid AppointmentRequest appointmentRequest) {
        User user = userCommonService.getUser();
        CounselorAppointment counselorAppointment = new CounselorAppointment();
        PatientRecord record = patientRecordRepository.findByPatientRecordId(appointmentRequest.getPatientRecordId()).orElseThrow();
        counselorAppointment.setPatientId(record.getPatientId());
        counselorAppointment.setStartDateTime(appointmentRequest.getStartDateTime());
        counselorAppointment.setEndDateTime(appointmentRequest.getEndDateTime());
        counselorAppointment.setCounselorId(user.getUserId());
        counselorAppointment.setPatientRecordId(appointmentRequest.getPatientRecordId());
        return counselorAppointment;
    }

    public AppointmentResponse toAppointmentResponse(Appointment appointment) {
        PatientRecord patientRecord = patientRecordRepository.findByPatientRecordId(appointment.getPatientRecordId()).orElseThrow(() -> new ResourceNotFoundException("Patient Record Not Found"));
        if (patientRecord != null) {
            AppointmentResponse appointmentResponse = new AppointmentResponse();
            appointmentResponse.setPatientRecordId(appointment.getPatientRecordId());
            appointmentResponse.setCreatedAt(appointment.getCreatedAt());
            appointmentResponse.setStartDateTime(appointment.getStartDateTime());
            appointmentResponse.setEndDateTime(appointment.getEndDateTime());
            appointmentResponse.setPatient(userMapper.toUserResponse(userRepository.findByUserIdAndDeletedFalse(patientRecord.getPatientId())));
            appointmentResponse.setAppointmentId(appointment.getAppointmentId());
            return appointmentResponse;
        }
        throw new ResourceNotFoundException("active patient record doesn't found");

    }

    public DoctorAppointment fromAppointmentRequestToDoctorAppointment(AppointmentRequest appointmentRequest) {
        User user = userCommonService.getUser();
        DoctorAppointment doctorAppointment = new DoctorAppointment();
        doctorAppointment.setPatientId(user.getUserId());
        doctorAppointment.setStartDateTime(appointmentRequest.getStartDateTime());
        doctorAppointment.setEndDateTime(appointmentRequest.getEndDateTime());
        doctorAppointment.setDoctorId(user.getUserId());
        doctorAppointment.setPatientRecordId(appointmentRequest.getPatientRecordId());
        return doctorAppointment;
    }
}
