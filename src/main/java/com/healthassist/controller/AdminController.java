package com.healthassist.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthassist.common.AuthorityName;
import com.healthassist.converter.LocalDateTimeReadConverter;
import com.healthassist.request.LoginRequest;
import com.healthassist.response.AdminPatientReport;
import com.healthassist.response.AdminPatientReportParameters;
import com.healthassist.response.LoginResponse;
import com.healthassist.service.AdminService;
import com.healthassist.service.BaseService;
import com.healthassist.request.UserRequest;
import com.healthassist.response.AdminPatientCard;
import com.healthassist.response.AdminUserCreateResponse;
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
    @PostMapping(value = "/patient")
    public AdminUserCreateResponse createPatient(@Valid @RequestBody UserRequest userRequest) {
        return adminService.createPatient(userRequest);
    }
}
