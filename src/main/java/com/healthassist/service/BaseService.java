package com.healthassist.service;

import com.healthassist.entity.User;
import com.healthassist.exception.AlreadyExistsException;
import com.healthassist.mapper.UserMapper;
import com.healthassist.repository.UserRepository;
import com.healthassist.request.UserRequest;
import com.healthassist.response.LoginResponse;
import com.healthassist.util.EncryptionUtil;
import com.healthassist.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class BaseService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    public LoginResponse signUp(UserRequest userRequest) {
        User user = userMapper.fromPatientRequest(userRequest);
        if (user == null) {
            return this.createErrorLoginResponse("Invalid user request");
        } else if (user.getEmailAddress() == null) {
            return this.createErrorLoginResponse("Invalid email address");
        } else if (user.getPassword() == null) {
            return this.createErrorLoginResponse("Invalid user password");
        } else if (user.getDateOfBirth() == null) {
            return this.createErrorLoginResponse("Invalid date of birth");
        } else if (user.getFullName() == null) {
            return this.createErrorLoginResponse("Invalid full name");
        } else if (user.getCity() == null) {
            return this.createErrorLoginResponse("Invalid city");
        } else if (user.getCountry() == null) {
            return this.createErrorLoginResponse("Invalid country");
        } else if (user.getPhoneNumber() == null) {
            return this.createErrorLoginResponse("Invalid phone number");
        } else if (user.getProvince() == null) {
            return this.createErrorLoginResponse("Invalid province");
        }
        try {
            this.checkIfEmailIsTakenWithException(user.getEmailAddress());
        } catch (AlreadyExistsException e) {
            return this.createErrorLoginResponse(e.getMessage());
        }
        
        user.setDeleted(false);
        user.setLastPasswordResetDate(new Date());
        //Encrypt User Password
        String encPassword = EncryptionUtil.encryptPassword(user.getPassword());
        if (encPassword != null) {
            user.setPassword(encPassword);
        }
        User savedUser = userRepository.save(user);
        return this.createSuccessLoginResponse(savedUser);
    }

    public LoginResponse createErrorLoginResponse() {
        return this.createErrorLoginResponse("Wrong credentials!");
    }

    public LoginResponse createErrorLoginResponse(String errorMessage) {
        LoginResponse response = new LoginResponse();
        response.setLoginSuccess(false);
        response.setErrorMessage(errorMessage);
        return response;
    }
    public LoginResponse createSuccessLoginResponse(User savedUser) {
        LoginResponse response = new LoginResponse();
        response.setUser(userMapper.toUserResponse(savedUser));
        response.setLoginSuccess(true);
        //TODO : set access token and status
        return response;
    }
    private boolean checkIfEmailIsTaken(String email) {
        return userRepository.existsByEmailAddressAndDeletedFalse(email);
    }

    public void checkIfEmailIsTakenWithException(String email) {
        if (this.checkIfEmailIsTaken(email)) {
            throw new AlreadyExistsException("User already exists");
        }
    }
}
