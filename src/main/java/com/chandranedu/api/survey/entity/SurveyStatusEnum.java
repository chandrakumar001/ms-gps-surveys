package com.chandranedu.api.survey.entity;

import java.util.Arrays;

import static com.chandranedu.api.error.exception.FieldValidationException.throwFieldValidationException;
import static com.chandranedu.api.util.StringUtils.isStringNullOrBlank;

public enum SurveyStatusEnum {

    ACTIVE,
    INACTIVE;

    public static final String INVALID_STATUS = "Invalid status, Available status are ACTIVE and INACTIVE";

    public static void checkSurveyStatusValidOrElseThrown(final String status) {
        if (validate(status)) {
            return;
        }
        throwFieldValidationException(INVALID_STATUS);
    }

    private static boolean validate(final String value) {
        return !isStringNullOrBlank(value) && isSurveyStatus(value);
    }

    private static boolean isSurveyStatus(final String value) {
        return Arrays.stream(SurveyStatusEnum.values())
                .anyMatch(status -> status.name().equals(value));
    }

    public static SurveyStatusEnum getSurveyStatus(final String status) {
        return SurveyStatusEnum.valueOf(status);
    }
}