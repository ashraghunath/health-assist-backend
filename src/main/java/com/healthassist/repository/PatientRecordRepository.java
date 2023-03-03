package com.healthassist.repository;

import com.healthassist.entity.PatientRecord;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PatientRecordRepository extends MongoRepository<PatientRecord, String> {
    Optional<PatientRecord> findByPatientRecordId(String patientRecordId);

    boolean existsByPatientRecordId(String patientRecordId);
}