package com.healthassist.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AppointmentListForDateRequest {
	    private LocalDateTime  date;	
}
