package com.chandranedu.api.survey.mapper;

import com.chandranedu.api.survey.entity.view.ResponsesAnswerQueryView;
import com.chandranedu.api.survey.entity.view.ResponsesQuestionQueryView;
import com.chandranedu.api.swagger.model.surveyresponses.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SurveyResponseSummaryMapper {

    public ResponsesQuestionsResponseDTO getResponsesQuestionsResponseDTO(
            final Set<ResponsesQuestionQueryView> questionsQueryView,
            final QuestionSummaryDTO summary) {

        final List<ResponsesQuestionDTO> questionsDTO = getQuestionsDTO(questionsQueryView);
        final ResponsesQuestionsResponseDTO questionsResponseDTO = new ResponsesQuestionsResponseDTO();
        questionsResponseDTO.setSummary(summary);
        questionsResponseDTO.setQuestions(questionsDTO);
        return questionsResponseDTO;
    }

    private List<ResponsesQuestionDTO> getQuestionsDTO(
            final Set<ResponsesQuestionQueryView> questionsQueryView) {
        return questionsQueryView.stream()
                .map(question -> {
                    final Set<ResponsesAnswerResponseDTO> answersDTO = getResponsesAnswerResponseDTOS(question);
                    return getQuestionDTO(answersDTO, question);
                }).collect(Collectors.toList());
    }

    private Set<ResponsesAnswerResponseDTO> getResponsesAnswerResponseDTOS(
            final ResponsesQuestionQueryView questionQueryView) {

        return questionQueryView.getAnswers()
                .stream()
                .map(SurveyResponseSummaryMapper::mapToResponsesAnswerResponseDTO)
                .collect(Collectors.toSet());
    }

    private static ResponsesAnswerResponseDTO mapToResponsesAnswerResponseDTO(
            final ResponsesAnswerQueryView answerQueryView) {

        final ResponsesAnswerResponseDTO answersDTO = new ResponsesAnswerResponseDTO();
        answersDTO.setAnswerId(answerQueryView.getAnswerId());
        answersDTO.setAnswerStatus(answerQueryView.getStatus().toString());
        answersDTO.setAnswerText(answerQueryView.getAnswerText());
        answersDTO.setIsCorrectAnswer(answerQueryView.getIsCorrectAnswer());
        answersDTO.setIsSelectedAnswer(answerQueryView.getIsSelectedAnswer());
        return answersDTO;
    }

    private ResponsesQuestionDTO getQuestionDTO(final Set<ResponsesAnswerResponseDTO> answersDTO,
                                                final ResponsesQuestionQueryView questionQueryView) {

        final ResponsesQuestionResponseDTO data = getQuestionResponseDTO(
                answersDTO,
                questionQueryView
        );
        final ResponsesQuestionDTO questionDTO = new ResponsesQuestionDTO();
        questionDTO.setQuestionId(questionQueryView.getQuestionId());
        questionDTO.setData(data);
        return questionDTO;
    }

    private ResponsesQuestionResponseDTO getQuestionResponseDTO(final Set<ResponsesAnswerResponseDTO> answersDTO,
                                                                final ResponsesQuestionQueryView questionQueryView) {

        final ResponsesQuestionResponseDTO data = new ResponsesQuestionResponseDTO();
        data.setAnswers(new ArrayList<>(answersDTO));
        data.setQuestionTitle(questionQueryView.getQuestionTitle());
        return data;
    }
}
