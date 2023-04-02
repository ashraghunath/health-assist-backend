package com.healthassist.response;

import java.util.List;

public class AdminPatientReport extends AdminPatientReportParameters {
    List<AdminPatientCard> patients;

    public List<AdminPatientCard> getPatients() {
        return patients;
    }

    public void setPatients(List<AdminPatientCard> patients) {
        this.patients = patients;
    }
}
