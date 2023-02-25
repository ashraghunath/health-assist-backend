package com.healthassist.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private String fullName;

    private String addressLine;

    private String city;

    private String province;

    private String country;

    private String phoneNumber;

    private String emailAddress;

    private Integer age;
}
