package com.chandranedu.api.survey.constant;

public class QuestionErrorMessageConstant {

    private QuestionErrorMessageConstant() {
        throw new IllegalStateException("QuestionErrorMessageConstant class");
    }

    public static final String ERROR_THE_QUESTION_ID_IS_INVALID_UUID_FORMAT = "Invalid.format.question.questionId";
    public static final String ERROR_QUESTION_ID_IS_NOT_FOUND = "NotFound.question.questionId";
    public static final String ERROR_ALREADY_THE_QUESTION_ID_IS_LINKED_WITH_SURVEY = "Already.survey.question.questionId";
    public static final String INVALID_QUESTION_OR_ANSWER_ID = "Invalid.questionId.answerId";

}
