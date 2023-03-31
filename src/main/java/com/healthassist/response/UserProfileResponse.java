package com.healthassist.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserProfileResponse {
    private String fullName;

    private String addressLine;

    private String city;

    private String province;

    private String country;

    private String phoneNumber;

    private String emailAddress;

    private LocalDateTime dateOfBirth;

    private String registrationNumber;
}
