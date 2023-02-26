package com.healthassist.common;

import com.healthassist.entity.User;
import org.springframework.stereotype.Service;
@Service
public class UserCommonService {
    public User getUser() {
        //need to retrieve user data based on the jwt token
        return new User() {
        };

    }
}
