package com.healthassist.controller;

import com.healthassist.request.AssessmentSubmissionRequest;
import com.healthassist.response.AssessmentResponse;
import com.healthassist.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/patient")
public class PatientController {

    @Autowired
    private AssessmentService assessmentService;

    @GetMapping("/assessment/{assessmentId}")
    public AssessmentResponse getAssessment(@PathVariable String assessmentId) {
        return assessmentService.getAssessment(assessmentId);
    }

    @PostMapping("/assessment/{assessmentId}")
    public void storeAssessment(@PathVariable String assessmentId, @Valid @RequestBody AssessmentSubmissionRequest assessmentSubmissionRequest) {
        assessmentService.storeAssessmentResult(assessmentId, assessmentSubmissionRequest);
    }

}
