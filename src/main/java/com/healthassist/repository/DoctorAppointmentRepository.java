package com.healthassist.repository;

import com.healthassist.entity.DoctorAppointment;
import com.healthassist.response.AppointmentListForDateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DoctorAppointmentRepository extends MongoRepository<DoctorAppointment, String> {
    DoctorAppointment findByAppointmentId(String appointmentId);

    Page<DoctorAppointment> findByDoctorIdAndStartDateTimeGreaterThanEqualOrderByCreatedAtDesc(String doctorId, LocalDateTime date, Pageable pageable);

    List<AppointmentListForDateResponse> findByDoctorIdAndStartDateTimeBetweenOrderByCreatedAtDesc(String counselorId, LocalDateTime startDate, LocalDateTime endDate);
    List<DoctorAppointment>findDoctorAppointmentByPatientRecordIdAndDoctorIdOrderByCreatedAtDesc(String patientRecordId,String doctorId);
    boolean existsByDoctorIdAndStartDateTimeBetweenOrStartDateTimeEquals(
            String doctorId, LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime startDateTimeE);

    boolean existsByDoctorIdAndEndDateTimeBetweenOrEndDateTimeEquals(
            String doctorId, LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime startDateTimeE);

    boolean existsByPatientRecordId(String patientRecordId);

    void deleteByAppointmentId(String appointmentId);

}