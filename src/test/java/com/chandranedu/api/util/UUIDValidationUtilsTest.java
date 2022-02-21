package com.chandranedu.api.util;

import com.chandranedu.api.error.exception.FieldValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.chandranedu.api.util.UUIDValidationUtils.validateAndConvertUUIDOrElseThrown;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UUIDValidationUtilsTest {

    @ParameterizedTest
    @ValueSource(
            strings = {"e2a40b33-a11a-4938-8111-07acf5758977", "e2a40b33-a11a-4938-8111-07acf5758978"}
    )
    @DisplayName("Valid uuid format")
    void validateAndConvertUUIDOrElseThrownTest(String id) {

        validateAndConvertUUIDOrElseThrown(id, "Invalid Id code");
    }

    @ParameterizedTest
    @ValueSource(
            strings = {"e2a40b33", "e2a40b33-a11a", "e2a40b33-a11a-4938-8111", "e2a40b33-a11a-4938-8111-07acf5758978888"}
    )
    @DisplayName("Invalid uuid format")
    void InvalidValidateAndConvertUUIDOrElseThrownTest(String id) {

        //given
        String errorMessage = "Invalid Id code";
        //when
        Exception exception = assertThrows(FieldValidationException.class, () -> {
            validateAndConvertUUIDOrElseThrown(id, errorMessage);
        });
        //then
        assertTrue(exception.getMessage().contains(errorMessage));
    }
}
