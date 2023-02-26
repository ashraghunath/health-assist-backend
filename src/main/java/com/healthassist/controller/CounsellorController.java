package com.healthassist.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	
	 @RequestMapping(value = "/login", method = RequestMethod.POST)
	    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
		 
	        return baseService.login(request, AuthorityName.ROLE_COUNSELOR);
	    }
	
}
