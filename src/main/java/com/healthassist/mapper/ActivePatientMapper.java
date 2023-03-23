package com.healthassist.mapper;

import com.healthassist.entity.ActivePatient;
import com.healthassist.entity.PatientRecord;
import com.healthassist.exception.ResourceNotFoundException;
import com.healthassist.repository.PatientRecordRepository;
import com.healthassist.repository.UserRepository;
import com.healthassist.response.PatientRecordCardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivePatientMapper {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PatientRecordRepository patientRecordRepository;

    public PatientRecordCardResponse toPatientRecordCardResponse(ActivePatient activePatient) {
        PatientRecordCardResponse cardResponse = new PatientRecordCardResponse();
        cardResponse.setPatient(userMapper.toUserCardResponse(userRepository.findByUserIdAndDeletedFalse(activePatient.getPatientId())));
        cardResponse.setPatientRecordId(activePatient.getPatientRecordId());
        cardResponse.setAssessmentCreatedAt(activePatient.getCreatedAt());
        PatientRecord patientRecord  = patientRecordRepository.findByPatientId(activePatient.getPatientId()).orElseThrow(()-> 
        new ResourceNotFoundException("No such patient"));
        cardResponse.setAppointmentId(patientRecord.getAppointmentId());
        return cardResponse;
    }
}
