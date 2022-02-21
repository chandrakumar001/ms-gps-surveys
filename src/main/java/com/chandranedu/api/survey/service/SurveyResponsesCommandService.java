package com.chandranedu.api.survey.service;

import com.chandranedu.api.swagger.model.surveyresponses.SurveyResponseDTO;
import com.chandranedu.api.swagger.model.surveyresponses.SurveyResponsesRequestDTO;

public interface SurveyResponsesCommandService {

    /**
     * Creates a new responses object.
     * <p>
     * If the id is not uuid format, a {@link com.chandranedu.api.error.exception.FieldValidationException} will be thrown.
     * If the id does not exist, a {@link com.chandranedu.api.error.exception.ResourceNotFoundException} will be thrown.
     *
     * @param reqSurveyId               the unique id of the Survey
     * @param surveyResponsesRequestDTO the user, selected question/answer submit object
     * @return created survey response fpr submit
     */
    SurveyResponseDTO createSurveyResponses(
            final String reqSurveyId,
            final SurveyResponsesRequestDTO surveyResponsesRequestDTO
    );
}
