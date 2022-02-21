package com.chandranedu.api.survey.util;

import com.chandranedu.api.survey.entity.Answer;
import com.chandranedu.api.survey.entity.Question;
import com.chandranedu.api.swagger.model.surveyresponses.ResponsesAnswerRequestDTO;
import com.chandranedu.api.swagger.model.surveyresponses.ResponsesQuestionRequestDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import static com.chandranedu.api.error.exception.ResourceNotFoundException.throwResourceNotFoundException;
import static com.chandranedu.api.survey.constant.QuestionErrorMessageConstant.INVALID_QUESTION_OR_ANSWER_ID;
import static com.chandranedu.api.survey.util.QuestionAnswerUtils.*;

@Component
public class QuestionAnswerValidator {


    public void validateQuestionsAndAnswers(final Map<UUID, Question> questionMap,
                                            final List<ResponsesQuestionRequestDTO> questionRequestDTOS) {

        final long questionCount = getQuestionCount(questionRequestDTOS, questionMap);
        if (questionCount == questionRequestDTOS.size()) {
            return;
        }
        throwResourceNotFoundException(INVALID_QUESTION_OR_ANSWER_ID);
    }

    private long getQuestionCount(final List<ResponsesQuestionRequestDTO> questionRequestDTOS,
                                  final Map<UUID, Question> questionMap) {

        return questionRequestDTOS.stream()
                .filter(isQuestionIdNotFoundPredicate(questionMap))
                .filter(isAnswerNotFoundPredicate(questionMap))
                .filter(isAnswerIdNotFoundPredicate(questionMap))
                .count();
    }

    private Predicate<ResponsesQuestionRequestDTO> isQuestionIdNotFoundPredicate(
            final Map<UUID, Question> questionMap) {
        return questionRequestDTO -> isQuestionId(questionMap, questionRequestDTO.getQuestionId());
    }

    private Predicate<ResponsesQuestionRequestDTO> isAnswerNotFoundPredicate(
            final Map<UUID, Question> questionMap) {
        return questionRequestDTO -> isAnswerFound(questionMap, questionRequestDTO.getQuestionId());
    }

    private Predicate<ResponsesQuestionRequestDTO> isAnswerIdNotFoundPredicate(
            final Map<UUID, Question> questionMap) {

        return questionRequestDTO -> {
            final Question question = getQuestion(questionMap, questionRequestDTO.getQuestionId());
            return isMatchAnswerIdAndCount(question, questionRequestDTO.getAnswers());
        };
    }

    private boolean isMatchAnswerIdAndCount(final Question question,
                                            final List<ResponsesAnswerRequestDTO> requestDTOAnswers) {
        //Considered answers is mandatory
        final Map<UUID, Answer> answerMap = getAnswersMap(question);
        final long answerCount = getCount(requestDTOAnswers, answerMap);
        return requestDTOAnswers.size() == answerCount;
    }

    private long getCount(final List<ResponsesAnswerRequestDTO> requestDTOAnswers,
                          final Map<UUID, Answer> answerMap) {
        return requestDTOAnswers.stream()
                .filter(isAnswerPredicate(answerMap))
                .count();
    }

    private Predicate<ResponsesAnswerRequestDTO> isAnswerPredicate(final Map<UUID, Answer> answerMap) {
        return answerRequestDTO -> isAnswerId(answerMap, answerRequestDTO.getAnswerId());
    }
}
