package com.healthassist.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("doctor_appointments")
public class DoctorAppointment extends Appointment{
    private String doctorId;
}
