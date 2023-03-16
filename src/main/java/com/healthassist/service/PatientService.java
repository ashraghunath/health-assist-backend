package com.healthassist.service;

import com.healthassist.common.AuthorityName;
import com.healthassist.common.PatientRecordStatus;
import com.healthassist.common.UserCommonService;
import com.healthassist.entity.*;
import com.healthassist.mapper.ActivePatientMapper;
import com.healthassist.repository.*;
import com.healthassist.response.AssessmentResultResponse;
import com.healthassist.response.PatientRecordCardResponse;
import com.healthassist.response.AttemptedQuestionResponse;

import java.util.ArrayList;
import java.util.List;

import com.healthassist.response.PatientRecordStatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    @Autowired
    ActivePatientRepository activePatientRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    ActivePatientMapper activePatientMapper;
    @Autowired
    AssessmentResultRepository assessmentResultRepository;
    @Autowired
    UserCommonService userCommonService;
    @Autowired
    PatientRecordRepository patientRecordRepository;
    @Autowired
    CounselorAppointmentRepository counselorAppointmentRepository;
    @Autowired
    DoctorAppointmentRepository doctorAppointmentRepository;
    
    public Page<PatientRecordCardResponse> getActivePatients(Pageable pageable) {
        Page<ActivePatient> activePatients =  activePatientRepository.findAll(pageable);
        return activePatients.map(activePatientMapper::toPatientRecordCardResponse);
    }
    
    public AssessmentResultResponse getAssessmentResult(String assessmentResultId) {
        AssessmentResultResponse assessmentResultResponse = new AssessmentResultResponse();
        AssessmentResult assessmentResult = assessmentResultRepository.findByAssessmentResultId(assessmentResultId);
        List<AttemptedQuestionResponse> attemptedQuestionResponses = new ArrayList<>();
        for (AttemptedQuestion attemptedQuestion : assessmentResult.getAttemptedQuestions()) {
            attemptedQuestionResponses.add(new AttemptedQuestionResponse(
                    questionRepository.findByQuestionId(attemptedQuestion.getQuestionId()).getQuestion(),
                    attemptedQuestion.getAnswer()
            ));
        }
        assessmentResultResponse.setAttemptedQuestions(attemptedQuestionResponses);
        return assessmentResultResponse;
    }

    public PatientRecordStatusResponse getPatientRecordStatus() {
        User user = userCommonService.getUser();
        return getPatientRecordStatus(user);
    }

    public PatientRecordStatusResponse getPatientRecordStatus(User user) {
        PatientRecordStatusResponse patientRecordStatusResponse =
                new PatientRecordStatusResponse();
        patientRecordStatusResponse.setPatientRecordStatus(PatientRecordStatus.NULL);
        if(!user.getAuthority().equals(AuthorityName.ROLE_PATIENT)) {
            return patientRecordStatusResponse;
        }
        String patientID = user.getUserId();
        PatientRecord patientRecord =
                patientRecordRepository.findTop1ByPatientIdOrderByCreatedAtDesc(patientID);
        if(patientRecord == null)
            return patientRecordStatusResponse;
        patientRecordStatusResponse.setPatientRecordStatus(patientRecord.getStatus());
        patientRecordStatusResponse.setCreatedAt(patientRecord.getCreatedAt());
        patientRecordStatusResponse.setUpdatedAt(patientRecord.getUpdatedAt());
        if(patientRecord.getStatus() == PatientRecordStatus.COUNSELOR_APPOINTMENT) {
            CounselorAppointment appointment = counselorAppointmentRepository.findByAppointmentId(patientRecord.getAppointmentId());
            patientRecordStatusResponse.setStartDateTime(appointment.getStartDateTime());
            patientRecordStatusResponse.setEndDateTime(appointment.getEndDateTime());
        }
        if(patientRecord.getStatus() == PatientRecordStatus.DOCTOR_APPOINTMENT) {
            DoctorAppointment appointment = doctorAppointmentRepository.findByAppointmentId(patientRecord.getAppointmentId());
            patientRecordStatusResponse.setStartDateTime(appointment.getStartDateTime());
            patientRecordStatusResponse.setEndDateTime(appointment.getEndDateTime());
        }
        return  patientRecordStatusResponse;
    }
}
