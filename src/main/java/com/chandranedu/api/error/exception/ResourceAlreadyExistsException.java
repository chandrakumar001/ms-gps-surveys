package com.chandranedu.api.error.exception;

public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(final String msg) {
        super(msg);
    }

    public static void throwResourceAlreadyExistsException(final String errorMessage) {
        throw new ResourceAlreadyExistsException(errorMessage);
    }
}
