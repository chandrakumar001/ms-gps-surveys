package com.chandranedu.api.survey.service;

public interface SurveyQuestionAssignCommandService {

    /**
     * Questions assign to Survey.
     * If the id is not uuid format, a {@link com.chandranedu.api.error.exception.FieldValidationException} will be thrown.
     * If the id does not exist, a {@link com.chandranedu.api.error.exception.ResourceNotFoundException} will be thrown.
     * If the id already assigned, a {@link com.chandranedu.api.error.exception.ResourceAlreadyExistsException} will be thrown.
     *
     * @param reqSurveyId Survey Id
     */
    void assignSurveyQuestionById(final String reqSurveyId);
}
