package com.healthassist.repository;

import com.healthassist.entity.DoctorAppointment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DoctorAppointmentRepository extends MongoRepository<DoctorAppointment, String> {
    DoctorAppointment findByAppointmentId(String appointmentId);
}