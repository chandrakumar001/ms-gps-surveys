package com.chandranedu.api.survey.service;

import com.chandranedu.api.swagger.model.question.QuestionDTO;
import com.chandranedu.api.swagger.model.question.QuestionRequestDTO;

public interface QuestionCommandService {

    /**
     * Creates a new Question.
     *
     * @param questionRequestDTO Question object
     * @return created Question
     */
    QuestionDTO createQuestion(final QuestionRequestDTO questionRequestDTO);

    /**
     * Delete a Question by the id.
     * If the id is not uuid format, a {@link com.chandranedu.api.error.exception.FieldValidationException} will be thrown.
     * If the id does not exist, a {@link com.chandranedu.api.error.exception.ResourceNotFoundException} will be thrown.
     *
     * @param reqQuestionId the unique id of the Question
     */
    void deleteQuestionById(final String reqQuestionId);
}
