package com.healthassist.response;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRecordResponse {
	String recordId;
	UserResponse patient;
	LocalDate createdAt;
	AssessmentResultResponse assessmentResult;
}