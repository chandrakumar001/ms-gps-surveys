package com.chandranedu.api.survey.service.impl;

import com.chandranedu.api.survey.entity.Question;
import com.chandranedu.api.survey.entity.SurveyQuestionAssign;
import com.chandranedu.api.survey.mapper.QuestionMapper;
import com.chandranedu.api.survey.repository.QuestionCommandRepository;
import com.chandranedu.api.survey.repository.SurveyQuestionAssignRepository;
import com.chandranedu.api.survey.service.QuestionCommandService;
import com.chandranedu.api.swagger.model.question.QuestionDTO;
import com.chandranedu.api.swagger.model.question.QuestionRequestDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.chandranedu.api.error.exception.ResourceAlreadyExistsException.throwResourceAlreadyExistsException;
import static com.chandranedu.api.error.exception.ResourceNotFoundException.throwResourceNotFoundException;
import static com.chandranedu.api.survey.constant.QuestionErrorMessageConstant.*;
import static com.chandranedu.api.util.UUIDValidationUtils.validateAndConvertUUIDOrElseThrown;


@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class DefaultQuestionCommandService implements QuestionCommandService {

    @NonNull QuestionCommandRepository questionCommandRepository;
    @NonNull SurveyQuestionAssignRepository surveyQuestionRepository;

    @NonNull QuestionMapper questionMapper;

    @Override
    public QuestionDTO createQuestion(final QuestionRequestDTO questionRequestDTO) {

        final Question question = questionMapper.mapToCreateQuestion(
                questionRequestDTO
        );
        final Question savedQuestion = saveQuestion(question);
        return questionMapper.mapToQuestionDTO(savedQuestion);
    }

    private Question saveQuestion(final Question question) {
        return questionCommandRepository.save(question);
    }

    @Override
    public void deleteQuestionById(final String reqQuestionId) {

        final UUID questionId = validateAndConvertUUIDOrElseThrown(
                reqQuestionId,
                ERROR_THE_QUESTION_ID_IS_INVALID_UUID_FORMAT
        );

        checkQuestionExistsOrElseThrown(questionId);
        checkQuestionAlreadyAssignedThenThrown(questionId);
        deleteQuestionById(questionId);
    }

    private void checkQuestionAlreadyAssignedThenThrown(final UUID questionId) {

        final List<SurveyQuestionAssign> surveyQuestionAssigns = getSurveyQuestionAssignOrElseThrown(
                questionId
        );
        if (CollectionUtils.isEmpty(surveyQuestionAssigns)) {
            return;
        }
        throwResourceAlreadyExistsException(ERROR_ALREADY_THE_QUESTION_ID_IS_LINKED_WITH_SURVEY);
    }

    private List<SurveyQuestionAssign> getSurveyQuestionAssignOrElseThrown(final UUID questionId) {
        return surveyQuestionRepository.findByQuestionQuestionId(questionId);
    }

    private void deleteQuestionById(final UUID questionId) {
        questionCommandRepository.deleteById(questionId);
    }

    private void checkQuestionExistsOrElseThrown(final UUID questionId) {

        final Optional<Question> questionOptional = getQuestionById(questionId);
        if (questionOptional.isEmpty()) {
            throwResourceNotFoundException(ERROR_QUESTION_ID_IS_NOT_FOUND);
        }
    }

    private Optional<Question> getQuestionById(final UUID questionId) {
        return questionCommandRepository.findById(questionId);
    }
}
