package com.healthassist.controller;

import com.healthassist.common.AuthorityName;
import com.healthassist.request.LoginRequest;
import com.healthassist.response.AssignedPatientResponse;
import com.healthassist.response.LoginResponse;
import com.healthassist.service.BaseService;
import com.healthassist.service.DoctorService;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/doctor")

public class DoctorController {
	@Autowired
	private DoctorService doctorService;

	@Autowired
	private BaseService baseService;

	@PostMapping("/login")
	public LoginResponse login(@Valid @RequestBody LoginRequest request) {
		return baseService.login(request, AuthorityName.ROLE_DOCTOR);
	}
	
	@RequestMapping(value = "/patient", method = RequestMethod.GET)
	public Page<AssignedPatientResponse> getAssignedPatients(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "10") Integer size) {
		Pageable paging = PageRequest.of(page, size);
		return doctorService.getAssignedPatients(paging);
	}
}
