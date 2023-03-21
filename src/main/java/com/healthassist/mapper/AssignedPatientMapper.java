package com.healthassist.mapper;

import com.healthassist.common.UserCommonService;
import com.healthassist.entity.AssessmentResult;
import com.healthassist.entity.AssignedPatient;
import com.healthassist.entity.DoctorAppointment;
import com.healthassist.entity.PatientRecord;
import com.healthassist.exception.ResourceNotFoundException;
import com.healthassist.repository.AssessmentResultRepository;
import com.healthassist.repository.DoctorAppointmentRepository;
import com.healthassist.repository.PatientRecordRepository;
import com.healthassist.repository.UserRepository;
import com.healthassist.response.AssignedPatientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
    DoctorAppointmentRepository appointmentRepository;
    @Autowired
    private AssessmentResultRepository assessmentResultRepository;

    public AssignedPatientResponse toAssignedPatientResponse(AssignedPatient assignedPatient) {
        if (patientRecordRepository.existsByPatientRecordId(assignedPatient.getPatientRecordId())) {
            PatientRecord patientRecord = patientRecordRepository.findByPatientRecordId(assignedPatient.getPatientRecordId())
            		.orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
            AssignedPatientResponse assignedPatientResponse = new AssignedPatientResponse();
            List<DoctorAppointment> appointments=appointmentRepository.findDoctorAppointmentByPatientRecordIdAndDoctorIdOrderByCreatedAtDesc(patientRecord.getPatientRecordId(),userCommonService.getUser().getUserId());
            //using first appointment for now
            if(appointments.size()>0)
                assignedPatientResponse.setAppointmentId(appointments.get(0).getAppointmentId());
            assignedPatientResponse.setPatientRecordId(assignedPatient.getPatientRecordId());
            assignedPatientResponse.setCounselor(userRepository.findFirstByRegistrationNumberAndDeletedFalse(assignedPatient.getCounselorRegistrationNumber()));
            assignedPatientResponse.setPatient(userMapper.toUserCardResponse(userRepository.findByUserIdAndDeletedFalse(patientRecord.getPatientId())));

            AssessmentResult assessmentResult = assessmentResultRepository.findByAssessmentResultId(patientRecord.getAssessmentResultId());
            assignedPatientResponse.setAssessmentCreatedAt(assessmentResult.getCreatedAt());
            assignedPatientResponse.setAssignedAt(assignedPatient.getCreatedAt());
            return assignedPatientResponse;
        }
        throw new ResourceNotFoundException("patient record doesn't found");
    }
}