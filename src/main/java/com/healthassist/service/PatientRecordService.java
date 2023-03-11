package com.healthassist.service;

import com.healthassist.common.PatientRecordStatus;
import com.healthassist.entity.ActivePatient;
import com.healthassist.entity.AssessmentResult;
import com.healthassist.entity.CounselorAppointment;
import com.healthassist.entity.PatientRecord;
import com.healthassist.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientRecordService {
    @Autowired
    AssessmentRepository assessmentRepository;

    @Autowired
    AssessmentResultRepository assessmentResultRepository;

    @Autowired
    ActivePatientRepository activePatientRepository;


    //TODO
    @Autowired
    PatientRecordRepository patientRecordRepository;

    public PatientRecord afterAssessment(AssessmentResult assessmentResult) {
        // store the patient record
        PatientRecord patientRecord = new PatientRecord();
        patientRecord.setAssessmentResultId(assessmentResult.getAssessmentResultId());
        patientRecord.setPatientId(assessmentResult.getPatientId());
        patientRecord.setStatus(PatientRecordStatus.COUNSELOR_IN_PROGRESS);
        patientRecord = patientRecordRepository.save(patientRecord);

        // create an active patient record
        ActivePatient activePatient = new ActivePatient();
        activePatient.setPatientId(assessmentResult.getPatientId());
        activePatient.setPatientRecordId(patientRecord.getPatientRecordId());
        activePatient = activePatientRepository.save(activePatient);

        patientRecord.setActivePatientId(activePatient.getActivePatientId());
        return patientRecordRepository.save(patientRecord);
    }

	public PatientRecord afterAppointment(CounselorAppointment counselorAppointment, PatientRecord patientRecord,
			PatientRecordStatus status) {
		// TODO Auto-generated method stub
		
		if (status == PatientRecordStatus.COUNSELOR_APPOINTMENT || status == PatientRecordStatus.DOCTOR_APPOINTMENT) {
            // update patient record (ActivePatient)
            patientRecord.update();
            patientRecord.setAppointmentId(counselorAppointment.getAppointmentId());
            patientRecord.setStatus(status);
            return patientRecordRepository.save(patientRecord);
        }
		return null;
		
	}
}
