package com.healthassist.response;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AppointmentListForDateResponse {
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
}
