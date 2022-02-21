package com.chandranedu.api.survey.service;

import com.chandranedu.api.swagger.model.survey.SurveyQuestionsResponseDTO;

public interface SurveyQuestionQueryService {

    /**
     * get a created Survey.
     * If the id is not uuid format, a {@link com.chandranedu.api.error.exception.FieldValidationException} will be thrown.
     * If the id does not exist, a {@link com.chandranedu.api.error.exception.ResourceNotFoundException} will be thrown.
     *
     * @param reqSurveyId Survey Id
     * @return created Survey
     */
    SurveyQuestionsResponseDTO getSurveyById(final String reqSurveyId);

    /**
     * get Survey and questions by the id.
     * If the id is not uuid format, a {@link com.chandranedu.api.error.exception.FieldValidationException} will be thrown.
     * If the id does not exist, a {@link com.chandranedu.api.error.exception.ResourceNotFoundException} will be thrown.
     *
     * @param reqSurveyId the unique id of the Survey
     * @return created Survey
     */
    SurveyQuestionsResponseDTO getSurveyResponsesById(final String reqSurveyId);

}
