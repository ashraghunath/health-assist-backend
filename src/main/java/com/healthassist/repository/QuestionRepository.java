package com.healthassist.repository;


import com.healthassist.entity.Question;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepository extends MongoRepository<Question, String> {
    Question findByQuestionId(String questionId);
    List<Question> findAll();
}