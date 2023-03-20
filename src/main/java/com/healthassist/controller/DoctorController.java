package com.healthassist.controller;

import com.healthassist.common.AuthorityName;
import com.healthassist.request.AppointmentListForDateRequest;
import com.healthassist.request.AppointmentRequest;
import com.healthassist.request.LoginRequest;
import com.healthassist.request.UserRequest;
import com.healthassist.response.*;
import com.healthassist.service.BaseService;
import com.healthassist.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/doctor")

public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private BaseService baseService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return baseService.login(request, AuthorityName.ROLE_DOCTOR);
    }

    @PostMapping("/signup")
    public LoginResponse signUp(@Valid @RequestBody UserRequest request) {
        return baseService.signUp(request, AuthorityName.ROLE_DOCTOR);
    }

    @RequestMapping(value = "/patient", method = RequestMethod.GET)
    public Page<AssignedPatientResponse> getAssignedPatients(@RequestParam(defaultValue = "0") Integer page,
                                                             @RequestParam(defaultValue = "10") Integer size) {
        Pageable paging = PageRequest.of(page, size);
        return doctorService.getAssignedPatients(paging);
    }

    @RequestMapping(value = "/patient/{patientRecordId}", method = RequestMethod.GET)
    public PatientRecordResponse getPatientRecord(@PathVariable String patientRecordId) {
        return doctorService.getActivePatient(patientRecordId);
    }

    @RequestMapping(value = "/patient/appointment", method = RequestMethod.GET)
    public Page<AppointmentResponse> getDoctorAppointments(@RequestParam(defaultValue = "0") Integer page,
                                                           @RequestParam(defaultValue = "10") Integer size) {
        Pageable paging = PageRequest.of(page, size);
        return doctorService.getDoctorAppointments(paging);
    }

    @RequestMapping(value = "/patient/appointments", method = RequestMethod.POST)
    public List<AppointmentListForDateResponse> getDoctorAppointmentsByDate(@Valid @RequestBody AppointmentListForDateRequest request) {
        return doctorService.getDoctorAppointmentsByDate(request);
    }

    @RequestMapping(value = "/patient/appointment", method = RequestMethod.POST)
    public void makeDoctorAppointment(@Valid @RequestBody AppointmentRequest appointmentRequest) {
        doctorService.storeDoctorAppointment(appointmentRequest);
    }

    @RequestMapping(value = "/patient/{patientRecordId}", method = RequestMethod.DELETE)
    public void rejectPatient(@PathVariable String patientRecordId) {
        doctorService.rejectAssignedPatient(patientRecordId);
    }
}