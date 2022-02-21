package com.chandranedu.api.survey.repository;

import com.chandranedu.api.survey.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface QuestionCommandRepository extends JpaRepository<Question, UUID> {
}