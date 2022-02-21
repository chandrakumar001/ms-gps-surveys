package com.chandranedu.api.survey.mapper.view;

import com.chandranedu.api.survey.entity.view.AnswerQueryView;
import com.chandranedu.api.swagger.model.question.AnswerResponseDTO;

public class AnswerQueryViewMapper {

    private AnswerQueryViewMapper() {
        throw new IllegalStateException("AnswerViewMapper class");
    }

    public static AnswerResponseDTO mapToAnswerRequestDTO(final AnswerQueryView answerQueryView) {

        final AnswerResponseDTO answerRequestDTO = new AnswerResponseDTO();
        answerRequestDTO.setAnswerId(answerQueryView.getAnswerId());
        answerRequestDTO.setAnswerText(answerQueryView.getAnswerText());
        answerRequestDTO.isCorrectAnswer(answerQueryView.getIsCorrectAnswer());
        return answerRequestDTO;
    }
}
