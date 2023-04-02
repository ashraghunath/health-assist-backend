package com.healthassist.controller;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return baseService.login(request, AuthorityName.ROLE_ADMIN);
    }
	
	@RequestMapping(value = "/report", method = RequestMethod.GET)
    public AdminPatientReport getAdminPatientReport(@RequestParam Long startDateTime,
                                                    @RequestParam Long endDateTime) {
        return adminService.getAdminPatientReportByRange(
                localDateTimeReadConverter.convert(startDateTime),
                localDateTimeReadConverter.convert(endDateTime));
    }

    @RequestMapping(value = "/report-parameters", method = RequestMethod.GET)
    public AdminPatientReportParameters getAdminPatientReportParameters() {
        return adminService.getAdminPatientReportParameters();
    }


}
