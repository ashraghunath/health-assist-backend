package com.healthassist.controller;

import javax.validation.Valid;

import com.healthassist.request.UserRequest;
import com.healthassist.response.PatientRecordCardResponse;
import com.healthassist.service.PatientService;
import com.healthassist.service.CounselorService;
import com.healthassist.response.PatientRecordResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.healthassist.common.AuthorityName;
import com.healthassist.request.AppointmentRequest;
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

	@Autowired
	private CounselorService counselorService;

	@PostMapping(value = "/login")
	public LoginResponse login(@Valid @RequestBody LoginRequest request) {
		return baseService.login(request, AuthorityName.ROLE_COUNSELOR);
	}

	@PostMapping(value = "/signup")
	public LoginResponse signup(@Valid @RequestBody UserRequest request) {

		return baseService.signUp(request, AuthorityName.ROLE_COUNSELOR);
	}

	@GetMapping(value = "/patient")
	public Page<PatientRecordCardResponse> getPatientList(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "10") Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		return patientService.getActivePatients(pageable);
	}

	@GetMapping(value = "/patient/{patientRecordId}")
	public PatientRecordResponse getPatientRecord(@PathVariable String patientRecordId) {
		return counselorService.getActivePatient(patientRecordId);
	}
	
	 @RequestMapping(value = "/patient/appointment", method = RequestMethod.POST)
	    public void makeCounselorAppointment(@Valid @RequestBody AppointmentRequest appointmentRequest) {
	        counselorService.storeCounselorAppointment(appointmentRequest);
	    }

//	@GetMapping(value = "/test")
//	public String test(){
//		return "hit the test call please";
//	}

}
