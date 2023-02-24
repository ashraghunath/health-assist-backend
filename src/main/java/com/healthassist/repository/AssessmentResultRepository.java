package com.healthassist.repository;

import com.healthassist.entity.AssessmentResult;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AssessmentResultRepository extends MongoRepository<AssessmentResult, String> {
    AssessmentResult findByAssessmentResultId(String assessmentResultId);

    List<AssessmentResult> findByPatientId(String patientId);

    void deleteByPatientId(String patientId);
}