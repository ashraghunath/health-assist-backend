package com.healthassist.mapper;

import com.healthassist.entity.User;
import com.healthassist.request.UserRequest;
import com.healthassist.response.UserCardResponse;
import com.healthassist.response.UserResponse;
import com.healthassist.util.TimeUtil;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
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
}
