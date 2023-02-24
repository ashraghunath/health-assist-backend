package com.healthassist.service;

import com.healthassist.request.UserRequest;
import com.healthassist.response.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BaseService {
    public LoginResponse signUp(UserRequest userRequest) {
        //TODO : Call signUp once it's implemented
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "This method is not yet implemented.");
    }
}
