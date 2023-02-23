package com.healthassist.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("questions")
@Setter
@Getter
public class Question {
    @Id
    private String questionId;

    private String question;

}