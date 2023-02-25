package com.healthassist.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.ZonedDateTime;
import java.util.Date;

@Getter
@Setter

public class User extends DateDomainObject {
    @Id
    private String userId;

    private String fullName;

    private String addressLine;

    private String city;

    private String province;

    private String country;

    private ZonedDateTime dateOfBirth;

    private String phoneNumber;

    @Indexed(unique = true)
    private String emailAddress;

    private String password;

    private boolean deleted;

    private Date lastPasswordResetDate;
}
