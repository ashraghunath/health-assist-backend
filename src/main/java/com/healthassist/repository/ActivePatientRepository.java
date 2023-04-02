package com.healthassist.repository;


import com.healthassist.entity.ActivePatient;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActivePatientRepository extends MongoRepository<ActivePatient, String> {

    boolean existsByPatientId(String patientId);
    Page<ActivePatient> findAll(Pageable pageable);

    ActivePatient findByActivePatientId(String activePatientId);

    void deleteByActivePatientId(String activePatientId);
	Integer countByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
	Integer countBy();
}