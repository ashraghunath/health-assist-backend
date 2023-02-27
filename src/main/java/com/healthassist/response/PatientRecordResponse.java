package com.healthassist.response;

import java.time.ZonedDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRecordResponse {
	String recordId;
	UserResponse patient;
	ZonedDateTime createdAt;
	AssessmentResultResponse assessmentResult;
}