package com.healthassist.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentRequest {
    private String patientRecordId;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;
}
