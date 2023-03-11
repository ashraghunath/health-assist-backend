package com.healthassist.response;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AssignedPatientResponse {
    private String patientRecordId;

    private UserCardResponse patient;

    private CounselorDoctorCardResponse counselor;

    private LocalDate assignedAt;

    private LocalDate assessmentCreatedAt;


}
