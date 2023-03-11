package com.healthassist.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CounselorDoctorCardResponse {
    private String registrationNumber;

    private String fullName;

    private String emailAddress;

    private String phoneNumber;

    private Integer currentPatients;

    
}
