package com.healthassist.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.healthassist.entity.CounselorAppointment;
import com.healthassist.response.AppointmentListForDateResponse;

public interface CounselorAppointmentRepository extends MongoRepository<CounselorAppointment, String> {

	boolean existsByPatientRecordId(String patientRecordId);

	boolean existsByCounselorIdAndStartDateTimeBetweenOrStartDateTimeEquals(String counselorId,
			LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime startDateTime2);

	boolean existsByCounselorIdAndEndDateTimeBetweenOrEndDateTimeEquals(String counselorId, LocalDateTime startDateTime,
			LocalDateTime endDateTime, LocalDateTime endDateTime2);

	List<AppointmentListForDateResponse> findByCounselorIdAndStartDateTimeBetweenOrderByCreatedAtDesc(String userId,
			LocalDateTime date, LocalDateTime plusDays);

}
