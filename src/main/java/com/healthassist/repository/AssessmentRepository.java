package com.healthassist.repository;

import com.healthassist.entity.Assessment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AssessmentRepository extends MongoRepository<Assessment, String> {
    Optional<Assessment> findByAssessmentId(String assessmentId);
}