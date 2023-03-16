package com.healthassist.mapper;

import com.healthassist.entity.User;
import com.healthassist.repository.AssignedPatientRepository;
import com.healthassist.request.UserRequest;
import com.healthassist.response.CounselorDoctorCardResponse;
import com.healthassist.response.UserCardResponse;
import com.healthassist.response.UserResponse;
import com.healthassist.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    @Autowired
    AssignedPatientRepository assignedPatientRepository;

    public User fromPatientRequest(UserRequest userRequest) {
        User user = new User();
        user.setFullName(userRequest.getFullName());
        user.setEmailAddress(userRequest.getEmailAddress());
        user.setPassword(userRequest.getPassword());
        user.setAddressLine(userRequest.getAddressLine());
        user.setCity(userRequest.getCity());
        user.setProvince(userRequest.getProvince());
        user.setCountry(userRequest.getCountry());
        user.setDateOfBirth(userRequest.getDateOfBirth());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setRegistrationNumber(userRequest.getRegistrationNumber());
        return user;
    }

    public UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setFullName(user.getFullName());
        response.setEmailAddress(user.getEmailAddress());
        response.setAddressLine(user.getAddressLine());
        response.setCity(user.getCity());
        response.setProvince(user.getProvince());
        response.setCountry(user.getCountry());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setAge(TimeUtil.nowUTC().getYear() - user.getDateOfBirth().getYear());
        return response;
    }

    public UserCardResponse toUserCardResponse(User user) {
        UserCardResponse userCardResponse = new UserCardResponse();
        userCardResponse.setEmailAddress(user.getEmailAddress());
        userCardResponse.setPhoneNumber(user.getPhoneNumber());
        userCardResponse.setFullName(user.getFullName());
        return userCardResponse;
    }

    public CounselorDoctorCardResponse toCounselorDoctorCardResponse(User user) {
        CounselorDoctorCardResponse response = new CounselorDoctorCardResponse();
        response.setFullName(user.getFullName());
        response.setEmailAddress(user.getEmailAddress());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setRegistrationNumber(user.getRegistrationNumber());
        if (user.getRegistrationNumber() != null)
            response.setCurrentPatients(assignedPatientRepository.countByDoctorRegistrationNumber(response.getRegistrationNumber()));
        return response;
    }
}
