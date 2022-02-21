package com.chandranedu.api.survey.service.impl;

import com.chandranedu.api.survey.entity.view.ResponsesQuestionQueryView;
import com.chandranedu.api.survey.entity.view.SurveyResponsesQueryView;
import com.chandranedu.api.survey.mapper.SurveyResponseSummaryMapper;
import com.chandranedu.api.survey.repository.SurveyResponsesQueryViewRepository;
import com.chandranedu.api.survey.service.ResponsesSummaryService;
import com.chandranedu.api.survey.service.SurveyResponsesQueryService;
import com.chandranedu.api.swagger.model.surveyresponses.QuestionSummaryDTO;
import com.chandranedu.api.swagger.model.surveyresponses.ResponsesQuestionsResponseDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.chandranedu.api.error.exception.ResourceNotFoundException.createResourceNotFoundException;
import static com.chandranedu.api.error.exception.ResourceNotFoundException.throwResourceNotFoundException;
import static com.chandranedu.api.survey.constant.SurveyErrorMessageConstant.*;
import static com.chandranedu.api.util.UUIDValidationUtils.validateAndConvertUUIDOrElseThrown;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DefaultSurveyResponsesQueryService implements SurveyResponsesQueryService {

    @NonNull SurveyResponsesQueryViewRepository summaryViewRepository;
    @NonNull ResponsesSummaryService responseSummaryService;
    @NonNull SurveyResponseSummaryMapper surveyResponseSummaryMapper;

    @Override
    public ResponsesQuestionsResponseDTO getSurveySummaryById(final String reqSurveyId,
                                                              final String reqResponsesId) {

        final UUID surveyId = validateAndConvertUUIDOrElseThrown(
                reqSurveyId,
                ERROR_THE_SURVEY_ID_IS_INVALID_UUID_FORMAT
        );
        final UUID responsesId = validateAndConvertUUIDOrElseThrown(
                reqResponsesId,
                ERROR_THE_RESPONSE_ID_IS_INVALID_UUID_FORMAT
        );

        final SurveyResponsesQueryView surveyResponsesQueryView = getSurveyResponsesOrElseThrown(
                surveyId,
                responsesId
        );
        final Set<ResponsesQuestionQueryView> questionsQueryView = surveyResponsesQueryView.getQuestions();
        if (CollectionUtils.isEmpty(questionsQueryView)) {
            throwResourceNotFoundException(ERROR_SURVEY_ID_AND_RESPONSE_ARE_NOT_FOUND);
        }

        final QuestionSummaryDTO summaryDTO = responseSummaryService.getQuestionSummaryDTO(
                questionsQueryView
        );
        return surveyResponseSummaryMapper.getResponsesQuestionsResponseDTO(questionsQueryView, summaryDTO);
    }

    private SurveyResponsesQueryView getSurveyResponsesOrElseThrown(
            final UUID surveyId,
            final UUID responseId) {
        return getSurveyResponsesById(surveyId, responseId)
                .orElseThrow(this::surveyResponseNotFoundException);
    }

    private Optional<SurveyResponsesQueryView> getSurveyResponsesById(final UUID surveyId,
                                                                      final UUID responseId) {
        return summaryViewRepository.findBySurveyResponseIdAndSurveyId(responseId, surveyId);
    }

    private RuntimeException surveyResponseNotFoundException() {
        return createResourceNotFoundException(ERROR_SURVEY_ID_AND_RESPONSE_ARE_NOT_FOUND);
    }
}
