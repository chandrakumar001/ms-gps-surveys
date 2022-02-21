package com.chandranedu.api.survey.service.impl;

import com.chandranedu.api.survey.entity.SurveyStatusEnum;
import com.chandranedu.api.survey.entity.view.SurveyQuestionQueryView;
import com.chandranedu.api.survey.mapper.view.SurveyQuestionQueryViewMapper;
import com.chandranedu.api.survey.repository.SurveyQuestionQueryViewRepository;
import com.chandranedu.api.survey.service.SurveyQuestionQueryService;
import com.chandranedu.api.swagger.model.survey.SurveyQuestionsResponseDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.chandranedu.api.error.exception.ResourceNotFoundException.createResourceNotFoundException;
import static com.chandranedu.api.survey.constant.SurveyErrorMessageConstant.ERROR_SURVEY_ID_IS_NOT_FOUND;
import static com.chandranedu.api.survey.constant.SurveyErrorMessageConstant.ERROR_THE_SURVEY_ID_IS_INVALID_UUID_FORMAT;
import static com.chandranedu.api.survey.entity.SurveyStatusEnum.ACTIVE;
import static com.chandranedu.api.util.UUIDValidationUtils.validateAndConvertUUIDOrElseThrown;


@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DefaultSurveyQuestionQueryService implements SurveyQuestionQueryService {

    @NonNull SurveyQuestionQueryViewRepository surveyQuestionQueryViewRepository;
    @NonNull SurveyQuestionQueryViewMapper surveyQuestionQueryViewMapper;

    @Override
    public SurveyQuestionsResponseDTO getSurveyById(final String reqSurveyId) {
        log.debug("getSurveyById called");

        final UUID surveyId = validateAndConvertUUIDOrElseThrown(
                reqSurveyId,
                ERROR_THE_SURVEY_ID_IS_INVALID_UUID_FORMAT
        );
        final SurveyQuestionQueryView existingSurveyQuestionsView = getSurveyQuestionViewOrElseThrown(
                surveyId
        );
        return surveyQuestionQueryViewMapper.mapToSurveyQuestionsResponse(existingSurveyQuestionsView);
    }

    private SurveyQuestionQueryView getSurveyQuestionViewOrElseThrown(final UUID surveyId) {
        return getSurveyQuestionViewById(surveyId)
                .orElseThrow(this::surveyNotFoundException);
    }

    private Optional<SurveyQuestionQueryView> getSurveyQuestionViewById(final UUID surveyId) {
        return surveyQuestionQueryViewRepository.findById(surveyId);
    }

    private RuntimeException surveyNotFoundException() {
        return createResourceNotFoundException(ERROR_SURVEY_ID_IS_NOT_FOUND);
    }

    @Override
    public SurveyQuestionsResponseDTO getSurveyResponsesById(final String reqSurveyId) {
        log.debug("getSurveyResponsesById called");

        final UUID surveyId = validateAndConvertUUIDOrElseThrown(
                reqSurveyId,
                ERROR_THE_SURVEY_ID_IS_INVALID_UUID_FORMAT
        );
        final SurveyQuestionQueryView existingSurveyQuestionsView = getSurveyByIdAndStatusOrElseThrown(surveyId, ACTIVE);
        return surveyQuestionQueryViewMapper.mapToSurveyQuestionsResponse(existingSurveyQuestionsView);
    }

    private SurveyQuestionQueryView getSurveyByIdAndStatusOrElseThrown(final UUID surveyId,
                                                                       final SurveyStatusEnum surveyStatusEnum) {
        return getSurveyByIdAndStatus(surveyId, surveyStatusEnum)
                .orElseThrow(this::surveyNotFoundException);
    }

    private Optional<SurveyQuestionQueryView> getSurveyByIdAndStatus(final UUID surveyId,
                                                                     final SurveyStatusEnum surveyStatusEnum) {
        return surveyQuestionQueryViewRepository.findBySurveyStatus(surveyId, surveyStatusEnum);
    }
}
