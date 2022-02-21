package com.chandranedu.api.survey.service.impl;

import com.chandranedu.api.survey.entity.view.QuestionQueryView;
import com.chandranedu.api.survey.mapper.view.QuestionQueryViewMapper;
import com.chandranedu.api.survey.repository.QuestionQueryViewRepository;
import com.chandranedu.api.survey.service.QuestionQueryService;
import com.chandranedu.api.swagger.model.question.QuestionDTO;
import com.chandranedu.api.swagger.model.question.QuestionsResponseDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.chandranedu.api.error.exception.NoRecordFoundException.throwNoRecordFoundException;
import static com.chandranedu.api.error.exception.ResourceNotFoundException.createResourceNotFoundException;
import static com.chandranedu.api.survey.constant.CommonConstant.NO_RECORD_FOUND;
import static com.chandranedu.api.survey.constant.QuestionErrorMessageConstant.ERROR_QUESTION_ID_IS_NOT_FOUND;
import static com.chandranedu.api.survey.constant.QuestionErrorMessageConstant.ERROR_THE_QUESTION_ID_IS_INVALID_UUID_FORMAT;
import static com.chandranedu.api.util.UUIDValidationUtils.validateAndConvertUUIDOrElseThrown;


@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultQuestionQueryService implements QuestionQueryService {

    @NonNull QuestionQueryViewRepository questionQueryViewRepository;
    @NonNull QuestionQueryViewMapper questionQueryViewMapper;

    @Override
    public QuestionsResponseDTO getQuestions() {

        final List<QuestionQueryView> questions = getAllQuestions();
        if (questions.isEmpty()) {
            throwNoRecordFoundException(NO_RECORD_FOUND);
        }
        return questionQueryViewMapper.mapToQuestionResponseDTO(questions);
    }

    private List<QuestionQueryView> getAllQuestions() {
        return questionQueryViewRepository.findAll();
    }

    @Override
    public QuestionDTO getQuestionById(final String reqQuestionId) {

        final UUID questionId = validateAndConvertUUIDOrElseThrown(
                reqQuestionId,
                ERROR_THE_QUESTION_ID_IS_INVALID_UUID_FORMAT
        );

        final QuestionQueryView existingQuestion = getQuestionOrElseThrown(
                questionId
        );
        return questionQueryViewMapper.mapToQuestionDTO(existingQuestion);
    }

    private QuestionQueryView getQuestionOrElseThrown(final UUID questionId) {
        return getQuestionById(questionId)
                .orElseThrow(this::questionNotFoundException);
    }

    private Optional<QuestionQueryView> getQuestionById(final UUID questionId) {
        return questionQueryViewRepository.findById(questionId);
    }

    private RuntimeException questionNotFoundException() {
        return createResourceNotFoundException(ERROR_QUESTION_ID_IS_NOT_FOUND);
    }
}
