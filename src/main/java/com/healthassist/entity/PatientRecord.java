package com.healthassist.entity;

import com.healthassist.common.PatientRecordStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("patient_records")
@Getter
@Setter
public class PatientRecord extends DateDomainObject {
    @Id
    private String patientRecordId;

    @Indexed(unique = true)
    private String assessmentResultId;

    private String patientId;

    private PatientRecordStatus status = PatientRecordStatus.NULL;

    private String appointmentId;

    private String activePatientId;

    private String assignedPatientId;

}