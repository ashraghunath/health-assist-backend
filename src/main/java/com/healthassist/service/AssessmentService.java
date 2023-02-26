package com.healthassist.service;

import com.healthassist.entity.Assessment;
import com.healthassist.entity.AssessmentResult;
import com.healthassist.entity.AttemptedQuestion;
import com.healthassist.exception.AlreadyExistsException;
import com.healthassist.exception.ResourceNotFoundException;
import com.healthassist.repository.*;
import com.healthassist.request.AssessmentSubmissionRequest;
import com.healthassist.request.AttemptedQuestionRequest;
import com.healthassist.response.AssessmentResponse;
import com.healthassist.response.QuestionProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssessmentService {

    @Autowired
    PatientRecordService patientRecordService;
    @Autowired
    AssessmentResultRepository assessmentResultRepository;
    @Autowired
    ActivePatientRepository activePatientRepository;
    @Autowired
    AssignedPatientRepository assignedPatientRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;
    @Autowired
    private QuestionRepository questionRepository;

    public AssessmentResponse getAssessment(String assessmentId) {
        AssessmentResponse response = new AssessmentResponse();
        Assessment assessment = assessmentRepository.findByAssessmentId(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessments do not exist yet, will be added soon!"));
        for (String questionId : assessment.getQuestionIds()) {
            response.addQuestion(new QuestionProjection(questionRepository.findByQuestionId(questionId)));
        }
        return response;
    }

    public void storeAssessmentResult(String assessmentId, AssessmentSubmissionRequest assessmentRequest) {

        //TODO: Retrieve user ID from userCommonService;
        String userId = null;
//
        // Check if user already has an active or assigned patient file
        if (activePatientRepository.existsByPatientId(userId) ||
                assignedPatientRepository.existsByPatientId(userId)) {
            throw new AlreadyExistsException("A patient file already exists for this user");
        }

        AssessmentResult assessmentResult = new AssessmentResult();
        assessmentResult.setAssessmentId(assessmentId);
        List<AttemptedQuestion> attemptedQuestions = new ArrayList<>();
        for (int i = 0; i < assessmentRequest.getQuestions().size(); i++) {
            AttemptedQuestionRequest questionRequest = assessmentRequest.getQuestions().get(i);
            attemptedQuestions.add(new AttemptedQuestion(questionRequest.getQuestionId(), questionRequest.getAnswer()));
        }
        assessmentResult.setPatientId(userId);
        assessmentResult.setAttemptedQuestions(attemptedQuestions);
        assessmentResult = assessmentResultRepository.save(assessmentResult);

        // create a record for active patients
        patientRecordService.afterAssessment(assessmentResult);
    }

}
