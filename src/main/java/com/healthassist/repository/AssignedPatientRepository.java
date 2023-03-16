package com.healthassist.repository;

import com.healthassist.entity.AssignedPatient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AssignedPatientRepository extends MongoRepository<AssignedPatient, String> {

    boolean existsByPatientId(String patientId);

    Page<AssignedPatient> findByDoctorRegistrationNumberOrderByCreatedAtDesc(String doctorRegistrationNumber, Pageable pageable);

    Integer countByDoctorRegistrationNumber(String doctorRegistrationNumber);

    boolean existsByPatientRecordId(String patientRecordId);
}