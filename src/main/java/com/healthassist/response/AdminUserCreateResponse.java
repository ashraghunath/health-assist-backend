package com.healthassist.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserCreateResponse {
    boolean success;
    String errorMessage;
    UserResponse user;
}
