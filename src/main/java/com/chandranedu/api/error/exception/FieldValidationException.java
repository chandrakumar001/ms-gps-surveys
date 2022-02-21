package com.chandranedu.api.error.exception;

public class FieldValidationException extends RuntimeException {

    public FieldValidationException(final String msg) {
        super(msg);
    }

    public static void throwFieldValidationException(final String errorMessage) {
        throw new FieldValidationException(errorMessage);
    }
}
