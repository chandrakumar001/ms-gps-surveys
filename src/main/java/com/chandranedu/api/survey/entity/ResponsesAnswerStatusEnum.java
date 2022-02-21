package com.chandranedu.api.survey.entity;

public enum ResponsesAnswerStatusEnum {
    CORRECT_ANSWER,
    INCORRECT_ANSWER;

    public static ResponsesAnswerStatusEnum getResponsesAnswerStatusEnum(final Boolean isSelectedAnswer,
                                                                         final Boolean isCorrectAnswer) {

        if (isCorrectAnswer.equals(isSelectedAnswer)) {
            return CORRECT_ANSWER;
        }
        return INCORRECT_ANSWER;
    }
}
