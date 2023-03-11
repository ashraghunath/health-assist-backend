package com.healthassist.request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentRequest {
    private String patientRecordId;

    private LocalDate startDateTime;

    private LocalDate endDateTime;
}
