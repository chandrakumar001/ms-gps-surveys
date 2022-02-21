package com.chandranedu.api.error;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@Builder
@Getter
public class ApiError {

    private final int code;
    private final String type;
    private final String message;
    private final LocalDateTime timestamp;

    public static ApiError createApiErrorBadRequest(final String message) {
        return ApiError.builder()
                .type(BAD_REQUEST.getReasonPhrase())
                .code(BAD_REQUEST.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiError createApiErrorOKRequest(final String message) {
        return ApiError.builder()
                .type(OK.getReasonPhrase())
                .code(OK.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiError createApiErrorNotFoundRequest(final String message) {
        return ApiError.builder()
                .type(NOT_FOUND.getReasonPhrase())
                .code(NOT_FOUND.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiError createApiErrorConflictRequest(final String message) {
        return ApiError.builder()
                .type(CONFLICT.getReasonPhrase())
                .code(CONFLICT.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
