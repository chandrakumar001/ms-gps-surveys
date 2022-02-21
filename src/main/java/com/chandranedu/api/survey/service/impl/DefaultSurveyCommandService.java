package com.chandranedu.api.survey.service.impl;

import com.chandranedu.api.survey.entity.Survey;
import com.chandranedu.api.survey.mapper.SurveyMapper;
import com.chandranedu.api.survey.repository.SurveyCommandRepository;
import com.chandranedu.api.survey.service.SurveyCommandService;
import com.chandranedu.api.swagger.model.survey.SurveyDTO;
import com.chandranedu.api.swagger.model.survey.SurveyRequestDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.chandranedu.api.error.exception.ResourceNotFoundException.throwResourceNotFoundException;
import static com.chandranedu.api.survey.constant.SurveyErrorMessageConstant.ERROR_SURVEY_ID_IS_NOT_FOUND;
import static com.chandranedu.api.survey.constant.SurveyErrorMessageConstant.ERROR_THE_SURVEY_ID_IS_INVALID_UUID_FORMAT;
import static com.chandranedu.api.survey.entity.SurveyStatusEnum.checkSurveyStatusValidOrElseThrown;
import static com.chandranedu.api.util.UUIDValidationUtils.validateAndConvertUUIDOrElseThrown;


@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Transactional
public class DefaultSurveyCommandService implements SurveyCommandService {

    @NonNull SurveyCommandRepository surveyCommandRepository;
    @NonNull SurveyMapper surveyMapper;

    @Override
    public SurveyDTO createSurvey(final SurveyRequestDTO surveyRequestDTO) {
        log.debug("createSurvey called");

        checkSurveyStatusValidOrElseThrown(surveyRequestDTO.getStatus());
        final Survey survey = surveyMapper.mapToCreateSurvey(surveyRequestDTO);
        final Survey savedSurvey = saveSurvey(survey);
        return surveyMapper.mapToSurveyDTO(savedSurvey);
    }

    private Survey saveSurvey(final Survey survey) {
        return surveyCommandRepository.save(survey);
    }

    @Override
    public void deleteSurveyById(final String reqSurveyId) {
        log.debug("deleteSurveyById called");

        final UUID surveyId = validateAndConvertUUIDOrElseThrown(
                reqSurveyId,
                ERROR_THE_SURVEY_ID_IS_INVALID_UUID_FORMAT
        );
        checkSurveyExistsOrElseThrown(surveyId);
        deleteSurveyById(surveyId);
    }

    private void checkSurveyExistsOrElseThrown(final UUID surveyId) {
        final Optional<Survey> surveyOptional = getSurveyById(surveyId);
        if (surveyOptional.isEmpty()) {
            throwResourceNotFoundException(ERROR_SURVEY_ID_IS_NOT_FOUND);
        }
    }

    private Optional<Survey> getSurveyById(final UUID surveyId) {
        return surveyCommandRepository.findById(surveyId);
    }

    private void deleteSurveyById(final UUID surveyId) {
        surveyCommandRepository.deleteById(surveyId);
    }
}
