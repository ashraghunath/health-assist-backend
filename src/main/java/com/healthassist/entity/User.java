package com.healthassist.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
@Getter
@Setter

public class User {
    private String userId;

    private String fullName;

    private String addressLine;

    private String city;

    private String province;

    private String country;

    private ZonedDateTime dateOfBirth;

    private String phoneNumber;

    private String emailAddress;

    private String password;

}
