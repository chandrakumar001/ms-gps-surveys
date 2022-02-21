package com.chandranedu.api.survey.service;

import com.chandranedu.api.survey.entity.view.ResponsesQuestionQueryView;
import com.chandranedu.api.swagger.model.surveyresponses.QuestionSummaryDTO;

import java.util.Set;

public interface ResponsesSummaryService {

    /**
     * get a Responses Question Summary.
     *
     * @param questionsQueryView questionsQueryView object
     * @return created Question
     */
    QuestionSummaryDTO getQuestionSummaryDTO(final Set<ResponsesQuestionQueryView> questionsQueryView);
}
