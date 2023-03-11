package com.healthassist.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Document("counselor_appointments")
public class CounselorAppointment extends Appointment {
    private String counselorId;
}
