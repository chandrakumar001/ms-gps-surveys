package com.chandranedu.api.survey.service.impl;

import com.chandranedu.api.error.exception.FieldValidationException;
import com.chandranedu.api.survey.entity.*;
import com.chandranedu.api.survey.mapper.SurveyResponsesMapper;
import com.chandranedu.api.survey.repository.SurveyCommandRepository;
import com.chandranedu.api.survey.repository.SurveyResponsesRepository;
import com.chandranedu.api.survey.service.SurveyResponsesCommandService;
import com.chandranedu.api.survey.util.QuestionAnswerValidator;
import com.chandranedu.api.survey.validation.SurveyResponsesValidator;
import com.chandranedu.api.swagger.model.surveyresponses.ResponsesQuestionRequestDTO;
import com.chandranedu.api.swagger.model.surveyresponses.SurveyResponseDTO;
import com.chandranedu.api.swagger.model.surveyresponses.SurveyResponsesRequestDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.chandranedu.api.error.exception.ResourceNotFoundException.createResourceNotFoundException;
import static com.chandranedu.api.error.exception.ResourceNotFoundException.throwResourceNotFoundException;
import static com.chandranedu.api.survey.constant.SurveyErrorMessageConstant.ERROR_SURVEY_ID_IS_NOT_FOUND;
import static com.chandranedu.api.survey.constant.SurveyErrorMessageConstant.ERROR_THE_SURVEY_ID_IS_INVALID_UUID_FORMAT;
import static com.chandranedu.api.survey.entity.SurveyStatusEnum.ACTIVE;
import static com.chandranedu.api.util.UUIDValidationUtils.validateAndConvertUUIDOrElseThrown;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DefaultSurveyResponsesCommandService implements SurveyResponsesCommandService {

    @NonNull SurveyResponsesRepository surveyResponsesRepository;
    @NonNull SurveyCommandRepository surveyCommandRepository;

    @NonNull SurveyResponsesValidator surveyResponsesValidator;
    @NonNull QuestionAnswerValidator questionAnswerValidator;

    @NonNull SurveyResponsesMapper surveyResponsesMapper;

    @Override
    @Transactional
    public SurveyResponseDTO createSurveyResponses(
            final String reqSurveyId,
            final SurveyResponsesRequestDTO surveyResponsesRequestDTO) {
        log.debug("createSurveyResponses called");
        final UUID surveyId = validateAndConvertUUIDOrElseThrown(
                reqSurveyId,
                ERROR_THE_SURVEY_ID_IS_INVALID_UUID_FORMAT
        );

        final List<ResponsesQuestionRequestDTO> nonDuplicateQuestions = removeDuplicateQuestions(
                surveyResponsesRequestDTO
        );
        surveyResponsesValidator.validateSurveyResponsesRequestDTO(nonDuplicateQuestions)
                .ifPresent(FieldValidationException::throwFieldValidationException);
        final Survey survey = getSurveyByIdAndStatusOrElseThrown(surveyId, ACTIVE);
        //The survey object ensured that survey question is assigned or thrown
        checkIfQuestionAssignedOrElseThrown(survey.getSurveyQuestionAssigns());

        final Map<UUID, Question> questionMap = convertToQuestionMap(
                survey.getSurveyQuestionAssigns()
        );
        //TODO re-structure validation logic later
        questionAnswerValidator.validateQuestionsAndAnswers(
                questionMap,
                nonDuplicateQuestions
        );
        final SurveyResponses newSurveyResponses = surveyResponsesMapper.mapToSurveyResponses(
                nonDuplicateQuestions,
                survey,
                questionMap
        );
        final SurveyResponses savedSurveyResponses = saveSurveyResponses(newSurveyResponses);
        return surveyResponsesMapper.mapToSurveyResponseDTO(savedSurveyResponses);
    }

    private List<ResponsesQuestionRequestDTO> removeDuplicateQuestions(final SurveyResponsesRequestDTO surveyResponsesRequestDTO) {

        final Set<ResponsesQuestionRequestDTO> nonDuplicateQuestions = new HashSet<>(
                surveyResponsesRequestDTO.getQuestions()
        );
        return new ArrayList<>(nonDuplicateQuestions);
    }

    private SurveyResponses saveSurveyResponses(final SurveyResponses newSurveyResponses) {
        return surveyResponsesRepository.save(newSurveyResponses);
    }

    private Survey getSurveyByIdAndStatusOrElseThrown(final UUID surveyId,
                                                      final SurveyStatusEnum surveyStatusEnum) {
        return getSurveyByIdAndStatus(surveyId, surveyStatusEnum)
                .orElseThrow(this::surveyNotFoundException);
    }

    private Optional<Survey> getSurveyByIdAndStatus(final UUID surveyId,
                                                    final SurveyStatusEnum surveyStatusEnum) {
        return surveyCommandRepository.findBySurveyStatus(surveyId, surveyStatusEnum);
    }

    private RuntimeException surveyNotFoundException() {
        return createResourceNotFoundException(ERROR_SURVEY_ID_IS_NOT_FOUND);
    }

    private void checkIfQuestionAssignedOrElseThrown(
            final Set<SurveyQuestionAssign> surveyQuestionAssigns) {
        if (CollectionUtils.isEmpty(surveyQuestionAssigns)) {
            throwResourceNotFoundException(ERROR_SURVEY_ID_IS_NOT_FOUND);
        }
    }

    private static Map<UUID, Question> convertToQuestionMap(
            final Set<SurveyQuestionAssign> surveyQuestionAssigns) {
        return surveyQuestionAssigns.stream()
                .map(SurveyQuestionAssign::getQuestion)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Question::getQuestionId, Function.identity()));
    }
}
