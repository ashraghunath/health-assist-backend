package com.healthassist.response;

import java.time.LocalDateTime;

public class AdminPatientCard extends UserCardResponse {
    LocalDateTime createdAt;

    public AdminPatientCard(UserCardResponse userCardResponse){
        this.setFullName(userCardResponse.getFullName());
        this.setEmailAddress(userCardResponse.getEmailAddress());
        this.setPhoneNumber(userCardResponse.getPhoneNumber());
        this.setRegistrationNumber(userCardResponse.getRegistrationNumber());
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}