package com.healthassist.repository;

import java.time.LocalDate;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.healthassist.entity.CounselorAppointment;

public interface CounselorAppointmentRepository extends MongoRepository<CounselorAppointment, String>{

	boolean existsByPatientRecordId(String patientRecordId);

	boolean existsByCounselorIdAndStartDateTimeBetweenOrStartDateTimeEquals(String counselorId, LocalDate startDateTime,
			LocalDate endDateTime, LocalDate startDateTime2);

	boolean existsByCounselorIdAndEndDateTimeBetweenOrEndDateTimeEquals(String counselorId, LocalDate startDateTime,
			LocalDate endDateTime, LocalDate endDateTime2);
	
}
