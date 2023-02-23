package com.healthassist.service;

import com.healthassist.entity.Assessment;
import com.healthassist.entity.AssessmentResponse;
import com.healthassist.entity.QuestionProjection;
import com.healthassist.exception.ResourceNotFoundException;
import com.healthassist.repository.AssessmentRepository;
import com.healthassist.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssessmentService {

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

}
