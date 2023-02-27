package com.healthassist.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Document("assessment_results")
@Getter
@Setter
public class AssessmentResult extends DateDomainObject {
    @Id
    private String assessmentResultId;

    private String assessmentId;

    private String patientId;

    private List<AttemptedQuestion> attemptedQuestions;
}
