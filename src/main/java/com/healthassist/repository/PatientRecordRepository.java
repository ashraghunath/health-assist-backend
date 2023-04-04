package com.healthassist.repository;

import com.healthassist.common.PatientRecordStatus;
import com.healthassist.entity.PatientRecord;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PatientRecordRepository extends MongoRepository<PatientRecord, String> {
    Optional<PatientRecord> findByPatientRecordId(String patientRecordId);
    void deletePatientRecordByPatientRecordId(String patientRecordId);

    boolean existsByPatientRecordId(String patientRecordId);
    PatientRecord findTop1ByPatientIdOrderByCreatedAtDesc(String userId);
	Optional<PatientRecord> findByPatientId(String patientId);

    List<PatientRecord> findByAppointmentIdAndStatus(String appointmentId, PatientRecordStatus patientRecordStatus);

    void deleteByPatientRecordId(String patientRecordId);

    void deleteByPatientId(String patientId);
}