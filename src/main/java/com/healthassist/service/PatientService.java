package com.healthassist.service;

import com.healthassist.entity.ActivePatient;
import com.healthassist.mapper.ActivePatientMapper;
import com.healthassist.repository.ActivePatientRepository;
import com.healthassist.response.AssessmentResultResponse;
import com.healthassist.response.PatientRecordCardResponse;
import com.healthassist.repository.QuestionRepository;
import com.healthassist.repository.AssessmentResultRepository;
import com.healthassist.entity.AssessmentResult;
import com.healthassist.entity.AttemptedQuestion;
import com.healthassist.response.AttemptedQuestionResponse;

import java.util.ArrayList;
import java.util.List;

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
}
