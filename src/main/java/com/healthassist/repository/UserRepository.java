package com.healthassist.repository;

import com.healthassist.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {

    boolean existsByEmailAddressAndDeletedFalse(String emailAddress);

}
