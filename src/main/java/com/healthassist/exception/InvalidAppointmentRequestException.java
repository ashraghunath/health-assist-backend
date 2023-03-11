package com.healthassist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidAppointmentRequestException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidAppointmentRequestException() {
        super();
    }

    public InvalidAppointmentRequestException(String message) {
        super(message);
    }

    public InvalidAppointmentRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
