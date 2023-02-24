package com.healthassist.service;

import com.healthassist.entity.AssessmentResult;
import com.healthassist.repository.AssessmentRepository;
import com.healthassist.repository.AssessmentResultRepository;
import com.healthassist.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientRecordService {
    @Autowired
    AssessmentRepository assessmentRepository;

    @Autowired
    AssessmentResultRepository assessmentResultRepository;

//    @Autowired
//    ActivePatientRepository activePatientRepository;

    //TODO Needs to be done as part of JWT
//    @Autowired
//    UserCommonService userCommonService;

    //TODO
//    @Autowired
//    PatientRecordRepository patientRecordRepository;

//    public PatientRecord afterAssessment(AssessmentResult assessmentResult) {
//        // store the patient record
//        PatientRecord patientRecord = new PatientRecord();
//        patientRecord.setAssessmentResultId(assessmentResult.getAssessmentResultId());
//        patientRecord.setPatientId(assessmentResult.getPatientId());
//        patientRecord.setStatus(PatientRecordStatus.COUNSELOR_IN_PROGRESS);
//        patientRecord = patientRecordRepository.save(patientRecord);
//
//        // create an active patient record
//        ActivePatient activePatient = new ActivePatient();
//        activePatient.setPatientId(assessmentResult.getPatientId());
//        activePatient.setPatientRecordId(patientRecord.getPatientRecordId());
//        activePatient = activePatientRepository.save(activePatient);
//
//        patientRecord.setActivePatientId(activePatient.getActivePatientId());
//        return patientRecordRepository.save(patientRecord);
//    }
}
