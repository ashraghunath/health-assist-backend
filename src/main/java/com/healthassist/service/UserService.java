package com.healthassist.service;


import com.healthassist.common.UserCommonService;
import com.healthassist.entity.User;
import com.healthassist.mapper.UserMapper;
import com.healthassist.repository.UserRepository;
import com.healthassist.request.UserUpdateRequest;
import com.healthassist.response.UserProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    UserRepository userRepository;

    public UserProfileResponse getProfileCard() {
        User user = userCommonService.getUser();
        return userMapper.toUserProfileResponse(user);
    }

    public UserProfileResponse updateProfile(UserUpdateRequest userUpdateRequest) {
        User updatedUser = userMapper.fromUserUpdateRequest(userUpdateRequest);
        return userMapper.toUserProfileResponse(userRepository.save(updatedUser));
    }
}
