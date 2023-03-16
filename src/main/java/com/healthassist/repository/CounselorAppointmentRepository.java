package com.healthassist.repository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.healthassist.entity.CounselorAppointment;
import com.healthassist.entity.PatientRecord;
import com.healthassist.response.AppointmentListForDateResponse;

public interface CounselorAppointmentRepository extends MongoRepository<CounselorAppointment, String> {

	boolean existsByPatientRecordId(String patientRecordId);

	boolean existsByCounselorIdAndStartDateTimeBetweenOrStartDateTimeEquals(String counselorId,
			LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime startDateTime2);

	boolean existsByCounselorIdAndEndDateTimeBetweenOrEndDateTimeEquals(String counselorId, LocalDateTime startDateTime,
			LocalDateTime endDateTime, LocalDateTime endDateTime2);

	List<AppointmentListForDateResponse> findByCounselorIdAndStartDateTimeBetweenOrderByCreatedAtDesc(String userId,
			LocalDateTime date, LocalDateTime plusDays);
	Page<CounselorAppointment> findByCounselorIdAndStartDateTimeGreaterThanEqualOrderByCreatedAtDesc(String counselorId,LocalDateTime date, Pageable pageable);

	CounselorAppointment findByAppointmentId(String appointmentId);
	

	void deleteByAppointmentId(String appointmentId);

}
