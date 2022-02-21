package com.chandranedu.api.survey.service.impl;

import com.chandranedu.api.survey.entity.Question;
import com.chandranedu.api.survey.entity.Survey;
import com.chandranedu.api.survey.entity.SurveyQuestionAssign;
import com.chandranedu.api.survey.mapper.SurveyQuestionMapperAssign;
import com.chandranedu.api.survey.repository.QuestionCommandRepository;
import com.chandranedu.api.survey.repository.SurveyCommandRepository;
import com.chandranedu.api.survey.repository.SurveyQuestionAssignRepository;
import com.chandranedu.api.survey.service.SurveyQuestionAssignCommandService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.chandranedu.api.error.exception.ResourceAlreadyExistsException.throwResourceAlreadyExistsException;
import static com.chandranedu.api.error.exception.ResourceNotFoundException.createResourceNotFoundException;
import static com.chandranedu.api.survey.constant.SurveyErrorMessageConstant.*;
import static com.chandranedu.api.util.UUIDValidationUtils.validateAndConvertUUIDOrElseThrown;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultSurveyQuestionAssignCommandService implements SurveyQuestionAssignCommandService {

    @NonNull QuestionCommandRepository questionCommandRepository;
    @NonNull SurveyCommandRepository surveyCommandRepository;
    @NonNull SurveyQuestionAssignRepository surveyQuestionAssignRepository;

    @NonNull SurveyQuestionMapperAssign assignMapper;

    @Override
    @Transactional
    public void assignSurveyQuestionById(final String reqSurveyId) {

        final UUID surveyId = validateAndConvertUUIDOrElseThrown(
                reqSurveyId,
                ERROR_THE_SURVEY_ID_IS_INVALID_UUID_FORMAT
        );

        checkSurveyQuestionAlreadyAssignedThrown(surveyId);
        final Survey existingSurvey = getSurveyOrElseThrown(surveyId);
        final List<Question> questions = questionCommandRepository.findAll();

        final Set<SurveyQuestionAssign> surveyQuestionAssigns = assignMapper.mapToSurveyQuestion(
                existingSurvey,
                questions
        );
        surveyQuestionAssignRepository.saveAll(surveyQuestionAssigns);
    }

    private void checkSurveyQuestionAlreadyAssignedThrown(final UUID surveyId) {

        final List<SurveyQuestionAssign> surveyQuestionAssigns = getAssignSurveyQuestions(
                surveyId
        );
        if (CollectionUtils.isEmpty(surveyQuestionAssigns)) {
            return;
        }
        throwResourceAlreadyExistsException(SURVEY_ID_IS_ALREADY_ASSIGNED);
    }

    private List<SurveyQuestionAssign> getAssignSurveyQuestions(final UUID surveyId) {
        return surveyQuestionAssignRepository.findBySurveySurveyId(surveyId);
    }

    private Survey getSurveyOrElseThrown(final UUID surveyId) {
        return getSurveyById(surveyId)
                .orElseThrow(this::surveyNotFoundException);
    }

    private Optional<Survey> getSurveyById(final UUID surveyId) {
        return surveyCommandRepository.findById(surveyId);
    }

    private RuntimeException surveyNotFoundException() {
        return createResourceNotFoundException(ERROR_SURVEY_ID_IS_NOT_FOUND);
    }
}
