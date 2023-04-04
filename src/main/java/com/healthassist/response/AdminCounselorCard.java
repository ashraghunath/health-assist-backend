package com.healthassist.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminCounselorCard extends UserCardResponse{
    LocalDateTime createdAt;

    public AdminCounselorCard(UserCardResponse userCardResponse) {
        this.setFullName(userCardResponse.getFullName());
        this.setEmailAddress(userCardResponse.getEmailAddress());
        this.setPhoneNumber(userCardResponse.getPhoneNumber());
        this.setRegistrationNumber(userCardResponse.getRegistrationNumber());
    }
}
