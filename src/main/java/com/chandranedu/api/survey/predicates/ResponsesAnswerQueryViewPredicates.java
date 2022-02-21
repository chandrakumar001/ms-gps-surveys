package com.chandranedu.api.survey.predicates;

import com.chandranedu.api.survey.entity.ResponsesAnswerStatusEnum;
import com.chandranedu.api.survey.entity.view.ResponsesAnswerQueryView;

import java.util.function.Predicate;

public class ResponsesAnswerQueryViewPredicates {

    private ResponsesAnswerQueryViewPredicates() {
        throw new IllegalStateException("ResponsesAnswerQueryViewPredicates class");
    }

    public static Predicate<ResponsesAnswerQueryView> isResponsesAnswerStatusEnum(
            final ResponsesAnswerStatusEnum answerStatusEnum) {
        return answerQueryView -> answerQueryView.getStatus().equals(answerStatusEnum);
    }
}
