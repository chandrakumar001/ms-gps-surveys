package com.chandranedu.api.error;

import com.chandranedu.api.error.exception.FieldValidationException;
import com.chandranedu.api.error.exception.NoRecordFoundException;
import com.chandranedu.api.error.exception.ResourceAlreadyExistsException;
import com.chandranedu.api.error.exception.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.chandranedu.api.error.ApiError.*;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GlobalExceptionHandler {

    private static final int ERROR_SIZE_ONE = 1;
    public static final int ERROR_DEFAULT_INDEX_SIZE = 0;

    @NonNull MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException methodArgumentNotValidException) {

        final BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        final List<ObjectError> allErrors = bindingResult.getAllErrors();

        if (allErrors.size() > ERROR_SIZE_ONE) {
            final List<ApiError> apiError = getCollect(allErrors);
            return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
        }

        final String message = getMessage(Objects.requireNonNull(allErrors.get(ERROR_DEFAULT_INDEX_SIZE).getCodes())[ERROR_DEFAULT_INDEX_SIZE]);
        final ApiError badRequest = createApiErrorBadRequest(message);
        return new ResponseEntity<>(badRequest, HttpStatus.BAD_REQUEST);

    }

    private List<ApiError> getCollect(final List<ObjectError> allErrors) {
        return allErrors.stream()
                .map(objectError -> getMessage(Objects.requireNonNull(objectError.getCodes())[ERROR_DEFAULT_INDEX_SIZE]))
                .map(errorMessage -> createApiErrorBadRequest(getMessage(errorMessage)))
                .collect(Collectors.toList());
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public final ResponseEntity<ApiError> handleHttpMediaTypeNotAcceptableException(
            final HttpMediaTypeNotAcceptableException httpMediaTypeNotAcceptableException,
            final WebRequest webRequest) {

        final String message = getMessage(httpMediaTypeNotAcceptableException.getMessage());
        final ApiError badRequest = createApiErrorBadRequest(message);
        return new ResponseEntity<>(badRequest, BAD_REQUEST);
    }

    @ExceptionHandler(FieldValidationException.class)
    public final ResponseEntity<ApiError> handleFieldValidationException(
            final FieldValidationException fieldValidationException) {

        final String message = getMessage(fieldValidationException.getMessage());
        final ApiError badRequest = createApiErrorBadRequest(message);
        return new ResponseEntity<>(badRequest, BAD_REQUEST);
    }

    @ExceptionHandler(NoRecordFoundException.class)
    public final ResponseEntity<ApiError> handleNoRecordFoundException(
            final NoRecordFoundException noRecordFoundException) {

        final String message = getMessage(noRecordFoundException.getMessage());
        final ApiError okRequest = createApiErrorOKRequest(message);
        return new ResponseEntity<>(okRequest, OK);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ApiError> handleResourceNotFoundException(
            final ResourceNotFoundException resourceNotFoundException) {

        final String message = getMessage(resourceNotFoundException.getMessage());
        final ApiError notFoundRequest = createApiErrorNotFoundRequest(message);
        return new ResponseEntity<>(notFoundRequest, NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public final ResponseEntity<ApiError> handleResourceAlreadyFoundException(
            final ResourceAlreadyExistsException resourceAlreadyExistsException) {

        final String message = getMessage(resourceAlreadyExistsException.getMessage());
        final ApiError conflictRequest = createApiErrorConflictRequest(message);
        return new ResponseEntity<>(conflictRequest, CONFLICT);
    }

    private String getMessage(final String message) {

        final Locale locale = LocaleContextHolder.getLocale();
        try {
            return messageSource.getMessage(message, null, locale);
        } catch (NoSuchMessageException e) {
            return message;
        }
    }
}
