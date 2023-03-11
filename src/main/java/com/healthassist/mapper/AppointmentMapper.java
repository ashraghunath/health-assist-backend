package com.healthassist.mapper;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.healthassist.common.UserCommonService;
import com.healthassist.entity.CounselorAppointment;
import com.healthassist.entity.User;
import com.healthassist.request.AppointmentRequest;

@Component
public class AppointmentMapper {

	@Autowired
	private UserCommonService userCommonService;

	public CounselorAppointment fromAppointmentRequestToCounselorAppointment(
			@Valid AppointmentRequest appointmentRequest) {
		User user = userCommonService.getUser();
		CounselorAppointment counselorAppointment = new CounselorAppointment();
		counselorAppointment.setPatientId(user.getUserId());
		counselorAppointment.setStartDateTime(appointmentRequest.getStartDateTime());
		counselorAppointment.setEndDateTime(appointmentRequest.getEndDateTime());
		counselorAppointment.setCounselorId(user.getUserId());
		counselorAppointment.setPatientRecordId(appointmentRequest.getPatientRecordId());
		return counselorAppointment;
	}

}
