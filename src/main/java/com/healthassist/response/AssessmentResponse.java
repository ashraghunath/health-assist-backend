package com.healthassist.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AssessmentResponse {
    private final List<QuestionProjection> questions = new ArrayList<>();

    public boolean addQuestion(QuestionProjection question) {
        return this.questions.add(question);
    }
}