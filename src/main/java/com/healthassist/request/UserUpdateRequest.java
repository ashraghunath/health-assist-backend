package com.healthassist.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserUpdateRequest {
    private String fullName;

    private String addressLine;

    private String city;

    private String province;

    private String country;

    private LocalDateTime dateOfBirth;

    private String phoneNumber;

    private String registrationNumber;


}
