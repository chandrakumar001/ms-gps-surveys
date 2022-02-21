package com.chandranedu.api.survey.service;

import com.chandranedu.api.swagger.model.question.QuestionDTO;
import com.chandranedu.api.swagger.model.question.QuestionsResponseDTO;

public interface QuestionQueryService {
    /**
     * get a created Question.
     * If the no data, a {@link com.chandranedu.api.error.exception.NoRecordFoundException} will be thrown.
     *
     * @return created Question
     */
    QuestionsResponseDTO getQuestions();

    /**
     * get a created Question.
     * If the id is not uuid format, a {@link com.chandranedu.api.error.exception.FieldValidationException} will be thrown.
     * If the id does not exist, a {@link com.chandranedu.api.error.exception.ResourceNotFoundException} will be thrown.
     *
     * @param reqQuestionId Question Id
     * @return created Question
     */
    QuestionDTO getQuestionById(final String reqQuestionId);

}
