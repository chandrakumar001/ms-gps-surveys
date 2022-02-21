package com.chandranedu.api.error.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(final String msg) {
        super(msg);
    }

    public static ResourceNotFoundException createResourceNotFoundException(final String errorMessage) {
        return new ResourceNotFoundException(errorMessage);
    }

    public static void throwResourceNotFoundException(final String errorMessage) {
        throw new ResourceNotFoundException(errorMessage);
    }
}
