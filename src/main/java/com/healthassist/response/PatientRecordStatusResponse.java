package com.healthassist.response;

import com.healthassist.common.PatientRecordStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PatientRecordStatusResponse {
    private PatientRecordStatus patientRecordStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public PatientRecordStatusResponse(PatientRecordStatus patientRecordStatus) {
        this.patientRecordStatus = patientRecordStatus;
    }
}
