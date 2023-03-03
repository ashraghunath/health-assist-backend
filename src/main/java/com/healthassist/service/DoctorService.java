package com.healthassist.service;

import com.healthassist.common.UserCommonService;
import com.healthassist.entity.AssignedPatient;
import com.healthassist.entity.PatientRecord;
import com.healthassist.exception.ResourceNotFoundException;
import com.healthassist.mapper.AssignedPatientMapper;
import com.healthassist.mapper.UserMapper;
import com.healthassist.repository.AssignedPatientRepository;
import com.healthassist.repository.PatientRecordRepository;
import com.healthassist.repository.UserRepository;
import com.healthassist.response.AssignedPatientResponse;
import com.healthassist.response.PatientRecordResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class DoctorService {
	@Autowired
	UserCommonService userCommonService;
	@Autowired
	AssignedPatientMapper assignedPatientMapper;
	@Autowired
	AssignedPatientRepository assignedPatientRepository;
	@Autowired
	PatientRecordRepository patientRecordRepository;
	@Autowired
	UserRepository userRepository;

	@Autowired
	UserMapper userMapper;
	@Autowired
	PatientService patientService;

    public Page<AssignedPatientResponse> getAssignedPatients(Pageable pageable) {
	     String userRegisterNumber = userCommonService.getUser().getRegistrationNumber();

        Page<AssignedPatient> assignedPatientPage = assignedPatientRepository.findByDoctorRegistrationNumberOrderByCreatedAtDesc(userRegisterNumber, pageable);

		return assignedPatientPage.map(assignedPatientMapper::toAssignedPatientResponse);
	}

	public PatientRecordResponse getActivePatient(String patientRecordId) {
		PatientRecord patientRecord = patientRecordRepository.findByPatientRecordId(patientRecordId)
				.orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
		PatientRecordResponse response = new PatientRecordResponse();
		response.setPatient(
				userMapper.toUserResponse(userRepository.findByUserIdAndDeletedFalse(patientRecord.getPatientId())));
		response.setRecordId(patientRecordId);
		response.setCreatedAt(patientRecord.getCreatedAt());
		response.setAssessmentResult(patientService.getAssessmentResult(patientRecord.getAssessmentResultId()));
		return response;
	}
}
