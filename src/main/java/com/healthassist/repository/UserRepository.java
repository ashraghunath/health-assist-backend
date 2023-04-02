package com.healthassist.repository;

import com.healthassist.common.AuthorityName;
import com.healthassist.entity.User;
import com.healthassist.response.CounselorDoctorCardResponse;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByEmailAddressAndDeletedFalse(String emailAddress);

    CounselorDoctorCardResponse findFirstByRegistrationNumberAndDeletedFalse(String registrationNumber);

    User findByUserIdAndDeletedFalse(String userId);

    User findByEmailAddress(String emailAddress);

    boolean existsByRegistrationNumberAndDeletedFalse(String registrationNumber);

    Page<User> findByAuthorityContainsAndDeletedFalseOrderByCreatedAtDesc(AuthorityName authorities, Pageable pageable);

	List<User> findByAuthorityContainsAndCreatedAtBetweenAndDeletedFalseOrderByCreatedAt(Set<AuthorityName> singleton,
			LocalDateTime startDateTime, LocalDateTime endDateTime);

	Integer countByAuthorityContains(Set<AuthorityName> singleton);
    
}
