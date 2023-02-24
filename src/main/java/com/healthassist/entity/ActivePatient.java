package com.healthassist.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Patients that have a submitted assessment
 */
@Document("active_patients")
@Getter
@Setter
public class ActivePatient extends DateDomainObject {
    @Id
    private String activePatientId;

    @Indexed(unique = true)
    private String patientId;

    @Indexed(unique = true)
    private String patientRecordId;
}