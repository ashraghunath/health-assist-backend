package com.healthassist.repository;

import com.healthassist.common.AuthorityName;
import com.healthassist.entity.User;
import com.healthassist.response.CounselorDoctorCardResponse;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByEmailAddressAndDeletedFalse(String emailAddress);

    CounselorDoctorCardResponse findFirstByRegistrationNumberAndDeletedFalse(String registrationNumber);

    User findByUserIdAndDeletedFalse(String userId);

    User findByEmailAddress(String emailAddress);

    boolean existsByRegistrationNumberAndDeletedFalse(String registrationNumber);

    User findByEmailAddressAndAuthorityAndDeletedFalse(String emailAddress, AuthorityName authorityNames);

    Page<User> findByAuthorityContainsAndDeletedFalseOrderByCreatedAtDesc(AuthorityName authority, Pageable pageable);

	List<User> findByAuthorityContainsAndCreatedAtBetweenAndDeletedFalseOrderByCreatedAt(AuthorityName authority,
			LocalDateTime startDateTime, LocalDateTime endDateTime);

	Integer countByAuthorityContains(AuthorityName authority);


    
}
