package com.healthassist.common;

import com.healthassist.entity.User;
import com.healthassist.exception.ResourceNotFoundException;
import com.healthassist.repository.UserRepository;
import com.healthassist.security.JwTService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserCommonService {
	@Autowired
    JwTService jwtService;
	
    @Autowired
    UserRepository userRepository;

    public User getUser() {
        String userId = jwtService.fetchLoggedInUserId();
        User user = userRepository.findByUserIdAndDeletedFalse(userId);
        if (user != null) {
            return user;
        } else {
            throw new ResourceNotFoundException("User not found!");
        }
    }
}
