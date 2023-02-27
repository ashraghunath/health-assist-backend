package com.healthassist.response;

import java.util.List;

import com.healthassist.response.AttemptedQuestionResponse;

public class AssessmentResultResponse {
    private List<AttemptedQuestionResponse> attemptedQuestions;

    public List<AttemptedQuestionResponse> getAttemptedQuestions() {
        return attemptedQuestions;
    }

    public void setAttemptedQuestions(List<AttemptedQuestionResponse> attemptedQuestions) {
        this.attemptedQuestions = attemptedQuestions;
    }

    public boolean addAttemptedQuestion(AttemptedQuestionResponse attemptedQuestion) {
        return this.attemptedQuestions.add(attemptedQuestion);
    }
}
