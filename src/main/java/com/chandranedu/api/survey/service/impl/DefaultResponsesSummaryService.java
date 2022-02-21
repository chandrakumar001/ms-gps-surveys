package com.chandranedu.api.survey.service.impl;

import com.chandranedu.api.survey.entity.ResponsesAnswerStatusEnum;
import com.chandranedu.api.survey.entity.view.ResponsesQuestionQueryView;
import com.chandranedu.api.survey.service.ResponsesSummaryService;
import com.chandranedu.api.swagger.model.surveyresponses.QuestionSummaryDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import static com.chandranedu.api.survey.entity.ResponsesAnswerStatusEnum.CORRECT_ANSWER;
import static com.chandranedu.api.survey.entity.ResponsesAnswerStatusEnum.INCORRECT_ANSWER;
import static com.chandranedu.api.survey.predicates.ResponsesAnswerQueryViewPredicates.isResponsesAnswerStatusEnum;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DefaultResponsesSummaryService implements ResponsesSummaryService {

    @Override
    public QuestionSummaryDTO getQuestionSummaryDTO(final Set<ResponsesQuestionQueryView> questionsQueryView) {

        final long totalQuestion = getTotalQuestionCount(questionsQueryView);
        final long totalIncorrectAnswer = getTotalIncorrectAnswerCount(questionsQueryView);
        final long totalCorrectAnswer = getTotalCorrectAnswerCount(questionsQueryView);

        final QuestionSummaryDTO summary = new QuestionSummaryDTO();
        summary.setTotalCorrectAnswer((int) totalCorrectAnswer);
        summary.setTotalIncorrectAnswer((int) totalIncorrectAnswer);
        summary.setTotalQuestion((int) totalQuestion);
        return summary;
    }

    private long getTotalQuestionCount(final Set<ResponsesQuestionQueryView> questionsQueryView) {

        return questionsQueryView.stream()
                .filter(Objects::nonNull)
                .map(ResponsesQuestionQueryView::getQuestionId)
                .count();
    }

    private long getTotalCorrectAnswerCount(final Set<ResponsesQuestionQueryView> questionsQueryView) {
        return getAnswerCount(questionsQueryView, CORRECT_ANSWER);
    }

    private long getTotalIncorrectAnswerCount(final Set<ResponsesQuestionQueryView> questionsQueryView) {
        return getAnswerCount(questionsQueryView, INCORRECT_ANSWER);
    }

    private long getAnswerCount(final Set<ResponsesQuestionQueryView> questionsQueryView,
                                final ResponsesAnswerStatusEnum answerStatusEnum) {

        return questionsQueryView.stream()
                .filter(Objects::nonNull)
                .map(ResponsesQuestionQueryView::getAnswers)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .filter(isResponsesAnswerStatusEnum(answerStatusEnum))
                .count();
    }
}
