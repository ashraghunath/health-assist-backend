package com.healthassist.response;

import java.time.ZonedDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AssignedPatientResponse {
    private String patientRecordId;

    private UserCardResponse patient;

    private CounselorDoctorCardResponse counselor;

    private ZonedDateTime assignedAt;

    private ZonedDateTime assessmentCreatedAt;


}
