package com.healthassist.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCardResponse {
    private String fullName;
    private String emailAddress;
    private String phoneNumber;
    private String registrationNumber;
}
