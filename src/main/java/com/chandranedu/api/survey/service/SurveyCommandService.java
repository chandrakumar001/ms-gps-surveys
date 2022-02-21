package com.chandranedu.api.survey.service;

import com.chandranedu.api.swagger.model.survey.SurveyDTO;
import com.chandranedu.api.swagger.model.survey.SurveyRequestDTO;

public interface SurveyCommandService {

    /**
     * Creates a new Survey.
     * If the status Invalid either ACTIVE and INACTIVE, a {@link com.chandranedu.api.error.exception.FieldValidationException} will be thrown.
     *
     * @param surveyRequestDTO Survey object
     * @return created Survey
     */
    SurveyDTO createSurvey(final SurveyRequestDTO surveyRequestDTO);

    /**
     * Delete a Survey by the id.
     * If the id is not uuid format, a {@link com.chandranedu.api.error.exception.FieldValidationException} will be thrown.
     * If the id does not exist, a {@link com.chandranedu.api.error.exception.ResourceNotFoundException} will be thrown.
     *
     * @param reqSurveyId the unique id of the Survey
     */
    void deleteSurveyById(final String reqSurveyId);
}
