package com.healthassist.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Getter
@Setter
public class PatientRecordCardResponse {
    private String patientRecordId;
    private UserCardResponse patient;
    private LocalDate assessmentCreatedAt;
}
