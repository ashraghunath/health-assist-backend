package com.healthassist.controller;

import com.healthassist.entity.AssessmentResponse;
import com.healthassist.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/patient")
public class PatientController {

    @Autowired
    private AssessmentService assessmentService;

    @GetMapping(value = "/assessment/{assessmentId}")
    public AssessmentResponse getAssessment(@PathVariable String assessmentId) {
        return assessmentService.getAssessment(assessmentId);
    }

}
