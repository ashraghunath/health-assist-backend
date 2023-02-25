package com.healthassist.repository;

import com.healthassist.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public interface UserRepository extends MongoRepository<User,String> {

}
