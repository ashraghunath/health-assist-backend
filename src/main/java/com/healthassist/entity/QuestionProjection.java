package com.healthassist.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionProjection {
    private final String questionId;
    private final String question;

    public QuestionProjection(Question question) {
        this.questionId = question.getQuestionId();
        this.question = question.getQuestion();
    }
}