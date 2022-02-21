package com.chandranedu.api.error.exception;

public class NoRecordFoundException extends RuntimeException {

    public NoRecordFoundException(final String msg) {
        super(msg);
    }

    public static void throwNoRecordFoundException(final String errorMessage) {
        throw new NoRecordFoundException(errorMessage);
    }
}
