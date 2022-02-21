package com.chandranedu.api.survey.repository;

import com.chandranedu.api.survey.entity.view.SurveyResponsesQueryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SurveyResponsesQueryViewRepository extends JpaRepository<SurveyResponsesQueryView, UUID> {

    Optional<SurveyResponsesQueryView> findBySurveyResponseIdAndSurveyId(
            final UUID responseId,
            final UUID surveyId
    );
}
