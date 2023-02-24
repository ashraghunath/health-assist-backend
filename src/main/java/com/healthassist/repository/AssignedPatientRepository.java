package com.healthassist.repository;

import com.healthassist.entity.AssignedPatient;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AssignedPatientRepository extends MongoRepository<AssignedPatient, String> {

    boolean existsByPatientId(String patientId);
}