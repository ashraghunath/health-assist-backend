package com.healthassist.service;

import com.healthassist.entity.ActivePatient;
import com.healthassist.mapper.ActivePatientMapper;
import com.healthassist.repository.ActivePatientRepository;
import com.healthassist.response.PatientRecordCardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    @Autowired
    ActivePatientRepository activePatientRepository;
    @Autowired
    ActivePatientMapper activePatientMapper;
    public Page<PatientRecordCardResponse> getActivePatients(Pageable pageable) {
        Page<ActivePatient> activePatients =  activePatientRepository.findAll(pageable);
        return activePatients.map(activePatientMapper::toPatientRecordCardResponse);
    }
}
