package com.healthassist.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("assessments")
@Getter
@Setter
public class Assessment extends DateDomainObject {
    @Id
    private String assessmentId;

    private List<String> questionIds;
}