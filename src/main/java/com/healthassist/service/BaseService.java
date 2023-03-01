package com.healthassist.service;

import com.healthassist.common.AuthorityName;
import com.healthassist.entity.User;
import com.healthassist.exception.AlreadyExistsException;
import com.healthassist.mapper.UserMapper;
import com.healthassist.repository.UserRepository;
import com.healthassist.request.UserRequest;
import com.healthassist.response.LoginResponse;
import com.healthassist.util.EncryptionUtil;
import com.healthassist.security.JwTService;
import com.healthassist.security.UserJWT;
import com.healthassist.request.LoginRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Service
public class BaseService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JwTService jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    public LoginResponse login(LoginRequest request, AuthorityName authorityName) {
        if (request == null) {
            return createErrorLoginResponse();
        }
        User savedUser = userRepository.findByEmailAddress(request.getEmailId().toLowerCase(Locale.ROOT));
        if (savedUser != null && savedUser.getAuthority().equals(authorityName)) {
            if (savedUser.isDeleted()) {
                return this.createErrorLoginResponse("Your account is permenantly deleted! Please contact administrator.");
            }
            if (checkValidLogin(savedUser, request.getPassword())) {
                return this.createSuccessLoginResponse(savedUser);
            } else {
                return this.createErrorLoginResponse();
            }
        } else {
            return this.createErrorLoginResponse("User does not exist. Continue with Registration.");
        }
    }
    
    private boolean checkValidLogin(User user, String password) {
        String userPassword = user.getPassword();
        return userPassword != null && EncryptionUtil.isValidPassword(password, userPassword);
    }

    
    
    public LoginResponse signUp(UserRequest userRequest, AuthorityName authorityName) {
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
        } else if (authorityName == AuthorityName.ROLE_COUNSELOR || authorityName == AuthorityName.ROLE_DOCTOR) {
            // If user has ROLE_COUNSELOR or ROLE_DOCTOR, then they are required to have UNIQUE registration number
            if (user.getRegistrationNumber() == null) {
                return this.createErrorLoginResponse("invalid registration number");
            }
            // check if the registration number is unique
            if (userRepository.existsByRegistrationNumberAndDeletedFalse(user.getRegistrationNumber())) {
                return this.createErrorLoginResponse("registration number is already in use");
            }
        }
        try {
            this.checkIfEmailIsTakenWithException(user.getEmailAddress());
        } catch (AlreadyExistsException e) {
            return this.createErrorLoginResponse(e.getMessage());
        }

        user.setAuthority(authorityName);
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
        return this.createErrorLoginResponse("Invalid username or password! Please try again.");
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

        UserJWT userDetails = (UserJWT) userDetailsService.loadUserByUsername(savedUser.getEmailAddress());
        response.setAccessToken(jwtTokenUtil.generateToken(userDetails));
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
