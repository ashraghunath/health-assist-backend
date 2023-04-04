package com.healthassist.controller;


import javax.validation.Valid;

import com.healthassist.entity.Assessment;
import com.healthassist.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthassist.common.AuthorityName;
import com.healthassist.converter.LocalDateTimeReadConverter;
import com.healthassist.request.LoginRequest;
import com.healthassist.service.AdminService;
import com.healthassist.service.BaseService;
import com.healthassist.request.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/admin")
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	LocalDateTimeReadConverter localDateTimeReadConverter;
	
	@Autowired
	BaseService baseService;

    @RequestMapping(value = "/assessment", method = RequestMethod.POST)
    public void createAssessment(@Valid @RequestBody Assessment assessment) {
        adminService.createAssessment(assessment);
    }
	
	@PostMapping(value = "/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return baseService.login(request, AuthorityName.ROLE_ADMIN);
    }
	
	@GetMapping(value = "/report")
    public AdminPatientReport getAdminPatientReport(@RequestParam Long startDateTime,
                                                    @RequestParam Long endDateTime) {
        return adminService.getAdminPatientReportByRange(
                localDateTimeReadConverter.convert(startDateTime),
                localDateTimeReadConverter.convert(endDateTime));
    }

    @GetMapping(value = "/report-parameters")
    public AdminPatientReportParameters getAdminPatientReportParameters() {
        return adminService.getAdminPatientReportParameters();
    }

    @GetMapping(value = "/patient")
    public Page<AdminPatientCard> getPatients(@RequestParam(defaultValue = "0") Integer page,
                                              @RequestParam(defaultValue = "10") Integer size) {
        Pageable paging = PageRequest.of(page, size);
        return adminService.getPatients(paging);
    }

    @RequestMapping(value = "/doctor", method = RequestMethod.GET)
    public Page<AdminDoctorCard> getDoctors(@RequestParam(defaultValue = "0") Integer page,
                                            @RequestParam(defaultValue = "10") Integer size) {
        Pageable paging = PageRequest.of(page, size);
        return adminService.getDoctors(paging);
    }


    @PostMapping(value = "/patient")
    public AdminUserCreateResponse createPatient(@Valid @RequestBody UserRequest userRequest) {
        return adminService.createPatient(userRequest);
    }

    @RequestMapping(value = "/doctor", method = RequestMethod.POST)
    public AdminUserCreateResponse createDoctor(@Valid @RequestBody UserRequest userRequest) {
        return adminService.createDoctor(userRequest);
    }

    @RequestMapping(value = "/doctor/{email}", method = RequestMethod.DELETE)
    public void removeDoctor(@PathVariable String email) {
        adminService.removeDoctor(email);
    }

    @RequestMapping(value = "/reset", method = RequestMethod.DELETE)
    public void resetUsers() {
        adminService.resetUsers();
    }
}
