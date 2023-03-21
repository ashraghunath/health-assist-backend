package com.healthassist.controller;

import com.healthassist.common.AuthorityName;
import com.healthassist.request.*;
import com.healthassist.response.*;
import com.healthassist.service.BaseService;
import com.healthassist.service.CounselorService;
import com.healthassist.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/counselor")
public class CounsellorController {

    @Autowired
    private BaseService baseService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private CounselorService counselorService;

    @PostMapping(value = "/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return baseService.login(request, AuthorityName.ROLE_COUNSELOR);
    }

    @PostMapping(value = "/signup")
    public LoginResponse signup(@Valid @RequestBody UserRequest request) {

        return baseService.signUp(request, AuthorityName.ROLE_COUNSELOR);
    }

    @GetMapping(value = "/patient")
    public Page<PatientRecordCardResponse> getPatientList(@RequestParam(defaultValue = "0") Integer page,
                                                          @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return patientService.getActivePatients(pageable);
    }

    @GetMapping(value = "/patient/{patientRecordId}")
    public PatientRecordResponse getPatientRecord(@PathVariable String patientRecordId) {
        return counselorService.getActivePatient(patientRecordId);
    }

    @PostMapping(value = "/patient/appointment")
    public void makeCounselorAppointment(@Valid @RequestBody AppointmentRequest appointmentRequest) {
        counselorService.storeCounselorAppointment(appointmentRequest);
    }

    @PostMapping(value = "/patient/appointments")
    public List<AppointmentListForDateResponse> getCounselorAppointmentsByDate(
            @Valid @RequestBody AppointmentListForDateRequest request) {
        return counselorService.getCounselorAppointmentsByDate(request);
    }

    @GetMapping(value = "/patient/appointment")
    public Page<AppointmentResponse> getAllAppointmentsForCounselor(@RequestParam(defaultValue = "0") Integer page,
                                                                    @RequestParam(defaultValue = "10") Integer size) {
        Pageable paging = PageRequest.of(page, size);
        return counselorService.getAllAppointmentForCounselor(paging);
    }

    @DeleteMapping(value = "/appointment/{appointmentId}")
    public void cancelAppointment(@PathVariable String appointmentId) {
        counselorService.cancelAppointment(appointmentId);
    }

    @GetMapping(value = "/doctor")
    public Page<CounselorDoctorCardResponse> getDoctorPage(@RequestParam(defaultValue = "0") Integer page,
                                                           @RequestParam(defaultValue = "10") Integer size) {
        Pageable paging = PageRequest.of(page, size);
        return counselorService.getDoctorPage(paging);
    }

    @PostMapping(value = "/doctor")
    public void assignDoctorToPatient(@Valid @RequestBody DoctorAssignmentRequest doctorAssignmentRequest) {
        counselorService.assignDoctorToPatient(doctorAssignmentRequest);
    }

	@DeleteMapping(value = "/patient/{patientRecordId}")
	public void rejectPatient(@PathVariable String patientRecordId) {
		counselorService.rejectPatient(patientRecordId);
	}

    @PutMapping(value = "/patient/appointment")
    public void editAppointment(@Valid @RequestBody AppointmentRequest appointmentRequest) {
        counselorService.editAppointment(appointmentRequest);
    }


}
