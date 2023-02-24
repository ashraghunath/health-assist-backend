package com.healthassist.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttemptedQuestion {
    private String questionId;

    private Integer answer;

    public AttemptedQuestion(String questionId, Integer answer) {
        this.questionId = questionId;
        this.answer = answer;
    }
}