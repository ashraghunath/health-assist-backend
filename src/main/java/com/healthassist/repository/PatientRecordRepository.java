package com.healthassist.repository;

import com.healthassist.entity.PatientRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PatientRecordRepository extends MongoRepository<PatientRecord, String> {
    PatientRecord findByPatientRecordId(String patientRecordId);

    boolean existsByPatientRecordId(String patientRecordId);
}