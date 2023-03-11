package com.healthassist.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Appointment extends DateDomainObject {

	    @Id
	    private String appointmentId;

	    @Indexed(unique = true)
	    private String patientRecordId;

	    private String patientId;

	    private LocalDate startDateTime;

	    private LocalDate endDateTime;

}
