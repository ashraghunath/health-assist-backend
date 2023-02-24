package com.healthassist.repository;

import com.healthassist.common.PatientRecordStatus;
import com.healthassist.entity.PatientRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PatientRecordRepository extends MongoRepository<PatientRecord, String> {

}