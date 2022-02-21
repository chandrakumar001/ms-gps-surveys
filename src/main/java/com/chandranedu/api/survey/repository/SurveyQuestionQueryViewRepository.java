package com.chandranedu.api.survey.repository;

import com.chandranedu.api.survey.entity.SurveyStatusEnum;
import com.chandranedu.api.survey.entity.view.SurveyQuestionQueryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface SurveyQuestionQueryViewRepository extends JpaRepository<SurveyQuestionQueryView, UUID> {

    @Query(value = "from #{#entityName} s where s.status=:status and s.surveyId=:surveyId")
    Optional<SurveyQuestionQueryView> findBySurveyStatus(
            @Param("surveyId") final UUID surveyId,
            @Param("status") final SurveyStatusEnum status
    );
}