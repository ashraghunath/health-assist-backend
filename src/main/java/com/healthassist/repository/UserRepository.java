package com.healthassist.repository;

import com.healthassist.entity.User;
import com.healthassist.response.CounselorDoctorCardResponse;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByEmailAddressAndDeletedFalse(String emailAddress);

    CounselorDoctorCardResponse findFirstByRegistrationNumberAndDeletedFalse(String registrationNumber);

    User findByUserIdAndDeletedFalse(String userId);

    User findByEmailAddress(String emailAddress);

}
