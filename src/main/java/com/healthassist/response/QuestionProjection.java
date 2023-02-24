package com.healthassist.response;

import com.healthassist.entity.Question;
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