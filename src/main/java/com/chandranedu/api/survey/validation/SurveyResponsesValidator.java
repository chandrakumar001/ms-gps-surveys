package com.chandranedu.api.survey.validation;

import com.chandranedu.api.swagger.model.surveyresponses.ResponsesAnswerRequestDTO;
import com.chandranedu.api.swagger.model.surveyresponses.ResponsesQuestionRequestDTO;
import com.chandranedu.api.util.UUIDValidationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class SurveyResponsesValidator {

    public static final String INVALID_QUESTION_ID = "Invalid questionId";
    public static final String INVALID_ANSWER_ID = "Invalid answerId";

    public Optional<String> validateSurveyResponsesRequestDTO(
            final List<ResponsesQuestionRequestDTO> questions) {

        final long questionCount = getQuestionCount(questions);
        if (questionCount == 0) {
            return Optional.of(INVALID_QUESTION_ID);
        }
        final long answerCount = getAnswerCount(questions);
        if (answerCount == 0) {
            return Optional.of(INVALID_ANSWER_ID);
        }
        return Optional.empty();
    }

    private long getAnswerCount(final List<ResponsesQuestionRequestDTO> questions) {

        if (CollectionUtils.isEmpty(questions)) {
            return 0;
        }
        return questions
                .stream()
                .map(ResponsesQuestionRequestDTO::getAnswers)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .map(ResponsesAnswerRequestDTO::getAnswerId)
                .filter(UUIDValidationUtils::validateUUID)
                .count();
    }

    private long getQuestionCount(final List<ResponsesQuestionRequestDTO> questions) {

        if (CollectionUtils.isEmpty(questions)) {
            return 0;
        }
        return questions
                .stream()
                .map(ResponsesQuestionRequestDTO::getQuestionId)
                .filter(UUIDValidationUtils::validateUUID)
                .count();
    }
}
