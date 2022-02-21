package com.chandranedu.api.survey.service;

import com.chandranedu.api.swagger.model.surveyresponses.ResponsesQuestionsResponseDTO;

public interface SurveyResponsesQueryService {

    /**
     * Get a responses object.
     * <p>
     * If the id is not uuid format, a {@link com.chandranedu.api.error.exception.FieldValidationException} will be thrown.
     * If the id does not exist, a {@link com.chandranedu.api.error.exception.ResourceNotFoundException} will be thrown.
     *
     * @param reqSurveyId    the unique id of the Survey
     * @param reqResponsesId the unique id of the responseId
     * @return created survey response fpr submit
     */
    ResponsesQuestionsResponseDTO getSurveySummaryById(
            final String reqSurveyId,
            final String reqResponsesId
    );
}
