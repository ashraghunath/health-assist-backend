package com.healthassist.controller;

import javax.validation.Valid;

import com.healthassist.response.PatientRecordCardResponse;
import com.healthassist.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.healthassist.common.AuthorityName;
import com.healthassist.request.LoginRequest;
import com.healthassist.response.LoginResponse;
import com.healthassist.service.BaseService;


@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/counselor")
public class CounsellorController {
	
	@Autowired
    private BaseService baseService;

	@Autowired
	private PatientService patientService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public LoginResponse login(@Valid @RequestBody LoginRequest request) {
		 
		return baseService.login(request, AuthorityName.ROLE_COUNSELOR);
	}
	@RequestMapping(value = "/patient",method = RequestMethod.GET)
	public Page<PatientRecordCardResponse> getPatientList(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		return patientService.getActivePatients(pageable);
	}
	
}
