package com.chandranedu.api.util;

import com.chandranedu.api.error.exception.FieldValidationException;

import java.util.Optional;
import java.util.UUID;

public final class UUIDValidationUtils {

    private UUIDValidationUtils() {
        throw new IllegalStateException("UUIDValidationUtil class");
    }

    public static UUID validateAndConvertUUIDOrElseThrown(final String id,
                                                          final String errorMessage) {

        validateUUID(id, errorMessage)
                .ifPresent(FieldValidationException::throwFieldValidationException);
        return UUID.fromString(id);
    }

    private static Optional<String> validateUUID(final String id,
                                                 final String errorMessage) {
        try {
            UUID.fromString(id);
            return Optional.empty();
        } catch (IllegalArgumentException illegalArgumentException) {
            return Optional.of(errorMessage);
        }
    }

    public static boolean validateUUID(final String id) {
        try {
            UUID.fromString(id);
            return true;
        } catch (IllegalArgumentException illegalArgumentException) {
            return false;
        }
    }
}
