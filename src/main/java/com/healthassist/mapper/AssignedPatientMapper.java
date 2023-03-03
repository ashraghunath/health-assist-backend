package com.healthassist.mapper;

import com.healthassist.common.UserCommonService;
import com.healthassist.entity.AssessmentResult;
import com.healthassist.entity.AssignedPatient;
import com.healthassist.entity.PatientRecord;
import com.healthassist.exception.ResourceNotFoundException;
import com.healthassist.repository.AssessmentResultRepository;
import com.healthassist.repository.PatientRecordRepository;
import com.healthassist.repository.UserRepository;
import com.healthassist.response.AssignedPatientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssignedPatientMapper {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserCommonService userCommonService;

    @Autowired
    private PatientRecordRepository patientRecordRepository;

    @Autowired
    private AssessmentResultRepository assessmentResultRepository;

    public AssignedPatientResponse toAssignedPatientResponse(AssignedPatient assignedPatient) {
        if (patientRecordRepository.existsByPatientRecordId(assignedPatient.getPatientRecordId())) {
            PatientRecord patientRecord = patientRecordRepository.findByPatientRecordId(assignedPatient.getPatientRecordId())
            		.orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
            AssignedPatientResponse appointmentResponse = new AssignedPatientResponse();

            appointmentResponse.setPatientRecordId(assignedPatient.getPatientRecordId());
            appointmentResponse.setCounselor(userRepository.findFirstByRegistrationNumberAndDeletedFalse(assignedPatient.getCounselorRegistrationNumber()));
            appointmentResponse.setPatient(userMapper.toUserCardResponse(userRepository.findByUserIdAndDeletedFalse(patientRecord.getPatientId())));

            AssessmentResult assessmentResult = assessmentResultRepository.findByAssessmentResultId(patientRecord.getAssessmentResultId());
            appointmentResponse.setAssessmentCreatedAt(assessmentResult.getCreatedAt());
            appointmentResponse.setAssignedAt(assignedPatient.getCreatedAt());
            return appointmentResponse;
        }
        throw new ResourceNotFoundException("patient record doesn't found");
    }
}