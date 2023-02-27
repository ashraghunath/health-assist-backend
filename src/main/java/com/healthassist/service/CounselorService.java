package com.healthassist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthassist.entity.PatientRecord;
import com.healthassist.response.PatientRecordResponse;
import com.healthassist.service.PatientService;
import com.healthassist.mapper.UserMapper;
import com.healthassist.repository.UserRepository;
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
	
	
    public PatientRecordResponse getActivePatient(String patientRecordId) {
        PatientRecord patientRecord = patientRecordRepository.findByPatientRecordId(patientRecordId);    
        PatientRecordResponse response = new PatientRecordResponse();
        response.setPatient(userMapper.toUserResponse(userRepository.findByUserIdAndDeletedFalse(patientRecord.getPatientId())));
        response.setRecordId(patientRecord.getPatientRecordId());
        response.setCreatedAt(patientRecord.getCreatedAt());
        response.setAssessmentResult(patientService.getAssessmentResult(patientRecord.getAssessmentResultId()));
        return response;
    }

}
