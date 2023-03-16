package com.healthassist.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DoctorAssignmentRequest {
    private String doctorRegistrationNumber;

    private String activePatientId;

}