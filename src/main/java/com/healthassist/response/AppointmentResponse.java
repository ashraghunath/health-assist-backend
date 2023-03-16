package com.healthassist.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentResponse {
	
		private String patientRecordId;
		
		private String appointmentId;

	    private UserResponse patient;

	    private LocalDateTime startDateTime;

	    private LocalDateTime endDateTime;

	    private LocalDateTime createdAt;
}