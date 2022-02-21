package com.chandranedu.api.survey.constant;

public class SurveyErrorMessageConstant {
    private SurveyErrorMessageConstant() {
        throw new IllegalStateException("SurveyErrorMessageConstant class");
    }

    public static final String ERROR_THE_RESPONSE_ID_IS_INVALID_UUID_FORMAT = "Invalid.format.response.responseId";
    public static final String ERROR_THE_SURVEY_ID_IS_INVALID_UUID_FORMAT = "Invalid.format.survey.surveyId";
    public static final String ERROR_SURVEY_ID_IS_NOT_FOUND = "NotFound.survey.surveyId";
    public static final String ERROR_SURVEY_ID_AND_RESPONSE_ARE_NOT_FOUND = "NotFound.survey.response.surveyId";
    public static final String SURVEY_ID_IS_ALREADY_ASSIGNED = "Already.surveyId.assigned";

}
