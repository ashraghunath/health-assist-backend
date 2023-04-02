package com.healthassist.service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthassist.entity.User;
import com.healthassist.mapper.UserMapper;
import com.healthassist.repository.ActivePatientRepository;
import com.healthassist.repository.AssignedPatientRepository;
import com.healthassist.repository.CounselorAppointmentRepository;
import com.healthassist.repository.DoctorAppointmentRepository;
import com.healthassist.repository.UserRepository;
import com.healthassist.response.AdminPatientReport;
import com.healthassist.response.AdminPatientReportParameters;
import com.healthassist.common.AuthorityName;


@Service
public class AdminService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserMapper userMapper;
	
	@Autowired
	ActivePatientRepository activePatientRepository;
	
	@Autowired
	CounselorAppointmentRepository counselorAppointmentRepository;
	
	@Autowired
	DoctorAppointmentRepository doctorAppointmentRepository;
	
	@Autowired
	AssignedPatientRepository assignedPatientRepository;
	
	
	 public AdminPatientReport getAdminPatientReportByRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
	        AdminPatientReport report = new AdminPatientReport();
	        List<User> patientCardPage = userRepository.findByAuthorityContainsAndCreatedAtBetweenAndDeletedFalseOrderByCreatedAt
	                (Collections.singleton(com.healthassist.common.AuthorityName.ROLE_PATIENT),
	                        startDateTime,
	                        endDateTime);
	        report.setPatients(patientCardPage.stream().map(userMapper::toAdminPatientCard).collect(Collectors.toList()));
	        report.setNumAttemptedAssessment(activePatientRepository.countByCreatedAtBetween(startDateTime, endDateTime));
	        report.setNumTotal(patientCardPage.size());
	        report.setNumHasCounselorAppointment(counselorAppointmentRepository.countByStartDateTimeBetween(startDateTime, endDateTime));
	        Integer numHasDoctorAppointment = doctorAppointmentRepository.countByStartDateTimeBetween(startDateTime, endDateTime);
	        report.setNumHasDoctorAppointment(numHasDoctorAppointment);
	        report.setNumInProcessingDoctorAppointment(assignedPatientRepository.countByCreatedAtBetween(startDateTime, endDateTime) - numHasDoctorAppointment);
	        return report;
	    }

	    public AdminPatientReportParameters getAdminPatientReportParameters() {
	        AdminPatientReportParameters report = new AdminPatientReportParameters();
	        Integer totalUsers = userRepository.countByAuthorityContains(Collections.singleton(AuthorityName.ROLE_PATIENT));
	        report.setNumAttemptedAssessment(activePatientRepository.countBy());
	        report.setNumTotal(totalUsers);
	        report.setNumHasCounselorAppointment(counselorAppointmentRepository.countBy());
	        Integer numHasDoctorAppointment = doctorAppointmentRepository.countBy();
	        report.setNumHasDoctorAppointment(numHasDoctorAppointment);
	        report.setNumInProcessingDoctorAppointment(assignedPatientRepository.countBy() - numHasDoctorAppointment);
	        return report;
	    }

}
