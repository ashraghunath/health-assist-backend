package com.healthassist.service;

import com.healthassist.common.UserCommonService;
import com.healthassist.entity.AssignedPatient;
import com.healthassist.mapper.AssignedPatientMapper;
import com.healthassist.repository.AssignedPatientRepository;
import com.healthassist.response.AssignedPatientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class DoctorService {
    @Autowired
    UserCommonService userCommonService;
    @Autowired
    AssignedPatientMapper assignedPatientMapper;
    @Autowired
    AssignedPatientRepository assignedPatientRepository;

    public Page<AssignedPatientResponse> getAssignedPatients(Pageable pageable) {
//      String userRegisterNumber = userCommonService.getUser().getRegistrationNumber();
        String userRegisterNumber = "123";
        Page<AssignedPatient> assignedPatientPage = assignedPatientRepository.findByDoctorRegistrationNumberOrderByCreatedAtDesc(userRegisterNumber, pageable);

        return assignedPatientPage.map(assignedPatientMapper::toAssignedPatientResponse);
    }
}
