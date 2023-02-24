package com.healthassist.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("assigned_patients")
@Getter
@Setter
public class AssignedPatient extends DateDomainObject {
    @Id
    private String assignedPatientId;

    private String doctorRegistrationNumber;

    @Indexed(unique = true)
    private String patientId;

    @Indexed(unique = true)
    private String patientRecordId;

    private String counselorRegistrationNumber;
}
