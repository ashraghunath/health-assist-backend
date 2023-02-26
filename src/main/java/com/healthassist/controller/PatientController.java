package com.healthassist.controller;

import com.healthassist.common.AuthorityName;
import com.healthassist.request.AssessmentSubmissionRequest;
import com.healthassist.request.UserRequest;
import com.healthassist.response.AssessmentResponse;
import com.healthassist.response.LoginResponse;
import com.healthassist.service.AssessmentService;
import com.healthassist.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/patient")
public class PatientController {

    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private BaseService baseService;

    @PostMapping("/signup")
    public LoginResponse signUp(@Valid @RequestBody UserRequest request) {
        return baseService.signUp(request, AuthorityName.ROLE_PATIENT);
    }

    @GetMapping("/assessment/{assessmentId}")
    public AssessmentResponse getAssessment(@PathVariable String assessmentId) {
        return assessmentService.getAssessment(assessmentId);
    }

    @PostMapping("/assessment/{assessmentId}")
    public void storeAssessment(@PathVariable String assessmentId, @Valid @RequestBody AssessmentSubmissionRequest assessmentSubmissionRequest) {
        assessmentService.storeAssessmentResult(assessmentId, assessmentSubmissionRequest);
    }

}
