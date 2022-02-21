package com.chandranedu.api.survey.repository;

import com.chandranedu.api.survey.entity.SurveyQuestionAssign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface SurveyQuestionAssignRepository extends JpaRepository<SurveyQuestionAssign, UUID> {

    List<SurveyQuestionAssign> findBySurveySurveyId(final UUID surveyId);

    List<SurveyQuestionAssign> findByQuestionQuestionId(final UUID questionId);
}