package com.chandranedu.api.survey.mapper.view;

import com.chandranedu.api.survey.entity.AuditMetadata;
import com.chandranedu.api.survey.entity.view.AnswerQueryView;
import com.chandranedu.api.survey.entity.view.QuestionQueryView;
import com.chandranedu.api.survey.entity.view.SurveyQuestionQueryView;
import com.chandranedu.api.swagger.model.survey.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class SurveyQuestionQueryViewMapper {

    public List<SurveyQuestionsResponseDTO> getSurveyQuestionsResponseDTO(
            final List<SurveyQuestionQueryView> allSurveyQuestions) {
        return allSurveyQuestions.stream()
                .map(this::mapToSurveyQuestionsResponse)
                .collect(Collectors.toList());
    }

    public SurveyQuestionsResponseDTO mapToSurveyQuestionsResponse(final SurveyQuestionQueryView surveyQuestionQueryView) {

        final List<SurveyQuestionDTO> questionsDTO = getQuestionsDTO(
                surveyQuestionQueryView.getQuestionAnswers()
        );
        final Map<UUID, List<SurveyAnswerResponseDTO>> answerRequestDTOMap = convertToAnswersResponseDTO(
                surveyQuestionQueryView.getQuestionAnswers()
        );
        final List<SurveyQuestionDTO> questionAnswerUpdated = getQuestionDTO(questionsDTO, answerRequestDTOMap);

        final SurveyDTO surveyDTO = mapToSurveyDTO(surveyQuestionQueryView);

        final SurveyQuestionsResponseDTO surveyQuestionsResponseDTO = new SurveyQuestionsResponseDTO();
        surveyQuestionsResponseDTO.setQuestions(questionAnswerUpdated);
        surveyQuestionsResponseDTO.setSurvey(surveyDTO);
        surveyQuestionsResponseDTO.setQuestionCount(questionsDTO.size());
        return surveyQuestionsResponseDTO;
    }

    private List<SurveyQuestionDTO> getQuestionDTO(
            final List<SurveyQuestionDTO> questionsDTO,
            final Map<UUID, List<SurveyAnswerResponseDTO>> answerRequestDTOMap) {
        return questionsDTO.stream()
                .peek(setAnswerAndGetQuestionDTOConsumer(answerRequestDTOMap))
                .collect(Collectors.toList());
    }

    private Consumer<SurveyQuestionDTO> setAnswerAndGetQuestionDTOConsumer(
            final Map<UUID, List<SurveyAnswerResponseDTO>> answerRequestDTOMap) {

        return questionDTO -> {
            final List<SurveyAnswerResponseDTO> queryView = answerRequestDTOMap.get(questionDTO.getQuestionId());
            final SurveyQuestionResponseDTO data = questionDTO.getData();
            data.setAnswers(queryView);
        };
    }

    private Map<UUID, List<SurveyAnswerResponseDTO>> convertToAnswersResponseDTO(
            final Set<QuestionQueryView> questionAnswers) {

        if (CollectionUtils.isEmpty(questionAnswers)) {
            return Collections.emptyMap();
        }
        return questionAnswers.stream()
                .map(QuestionQueryView::getAnswers)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.groupingBy(
                        AnswerQueryView::getQuestionId,
                        Collectors.mapping(this::mapToAnswerResponseDTO, Collectors.toList()))
                );
    }

    private List<SurveyQuestionDTO> getQuestionsDTO(final Set<QuestionQueryView> questionAnswersView) {

        if (CollectionUtils.isEmpty(questionAnswersView)) {
            return Collections.emptyList();
        }
        return questionAnswersView
                .stream()
                .map(this::getQuestionDTO)
                .collect(Collectors.toList());
    }

    private SurveyQuestionDTO getQuestionDTO(final QuestionQueryView questionQueryView) {

        final SurveyQuestionResponseDTO data = new SurveyQuestionResponseDTO();
        data.setText(questionQueryView.getQuestionTitle());

        final SurveyQuestionDTO questionDTO = new SurveyQuestionDTO();
        questionDTO.setData(data);
        questionDTO.setQuestionId(questionQueryView.getQuestionId());
        return questionDTO;
    }

    private SurveyAnswerResponseDTO mapToAnswerResponseDTO(final AnswerQueryView answerQueryView) {

        final SurveyAnswerResponseDTO answerResponseDTO = new SurveyAnswerResponseDTO();
        answerResponseDTO.setAnswerText(answerQueryView.getAnswerText());
        answerResponseDTO.setAnswerId(answerQueryView.getAnswerId());
        return answerResponseDTO;
    }

    public SurveyDTO mapToSurveyDTO(final SurveyQuestionQueryView questionAnswerView) {

        final AuditMetadata auditMetadata = questionAnswerView.getAuditMetadata();
        final SurveyResponseDTO data = mapToSurveysResponseDTO(questionAnswerView);

        final SurveyDTO surveyDTO = new SurveyDTO();
        surveyDTO.setData(data);
        surveyDTO.setSurveyId(questionAnswerView.getSurveyId());
        surveyDTO.setCreationDate(auditMetadata.getCreationDate().toString());
        surveyDTO.setUpdatedDate(auditMetadata.getUpdatedDate().toString());
        return surveyDTO;
    }

    private SurveyResponseDTO mapToSurveysResponseDTO(final SurveyQuestionQueryView surveyQuestionQueryView) {

        final SurveyResponseDTO data = new SurveyResponseDTO();
        data.setDescription(surveyQuestionQueryView.getDescription());
        data.setSurveyTitle(surveyQuestionQueryView.getSurveyTitle());
        data.setStatus(surveyQuestionQueryView.getStatus().toString());
        return data;
    }
}