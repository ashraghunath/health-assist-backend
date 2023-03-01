package com.healthassist.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    boolean loginSuccess;
    String errorMessage;
    String accessToken;
    UserResponse user;
    // TODO: Define properties for LoginResponse

    // TODO: Define constructors for LoginResponse

    // TODO: Define any additional methods for LoginResponse
}
