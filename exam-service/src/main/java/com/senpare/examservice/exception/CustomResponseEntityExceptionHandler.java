package com.senpare.examservice.exception;

import io.sentry.Sentry;
import io.sentry.SentryLevel;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = ResourceAlreadyExistsException.class)
    public ResponseEntity<Object> handleConflicts(RuntimeException exception, WebRequest request) {
        log.debug(exception.getMessage());
        Sentry.captureMessage(exception.getMessage(), SentryLevel.DEBUG);
        return handleExceptionInternal(exception, prepareErrorResponse(exception.getMessage(), null), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> handleBadRequests(RuntimeException exception, WebRequest request) {
        log.debug(exception.getMessage());
        Sentry.captureMessage(exception.getMessage(), SentryLevel.DEBUG);
        return handleExceptionInternal(exception, prepareErrorResponse(exception.getMessage(), null), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = ForbiddenException.class)
    public ResponseEntity<Object> handleForbiddenRequest(RuntimeException exception, WebRequest request) {
        log.debug(exception.getMessage());
        Sentry.captureMessage(exception.getMessage(), SentryLevel.DEBUG);
        return handleExceptionInternal(exception, prepareErrorResponse(exception.getMessage(), null), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(RuntimeException exception, WebRequest request) {
        log.debug(exception.getMessage());
        Sentry.captureMessage(exception.getMessage(), SentryLevel.DEBUG);
        return handleExceptionInternal(exception, prepareErrorResponse(exception.getMessage(), null), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        exception.getConstraintViolations().forEach((error) -> {
            errors.put(error.getPropertyPath().toString(), error.getMessage());
        });

        log.debug(exception.getMessage());
        Sentry.captureMessage(exception.getMessage(), SentryLevel.DEBUG);

        return this.handleExceptionInternal(exception, prepareErrorResponse("Invalid content in the request body", errors), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            errors.put(error.getObjectName(), error.getDefaultMessage());
        });

        log.debug(exception.getMessage());
        Sentry.captureMessage(exception.getMessage(), SentryLevel.DEBUG);

        return this.handleExceptionInternal(exception, prepareErrorResponse("Invalid content in the request body", errors), headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(LicenseViolationException.class)
    public ResponseEntity<Object> handleLicenseViolationException(LicenseViolationException exception, WebRequest request) {
        log.debug(exception.getMessage());
        Sentry.captureMessage(exception.getMessage(), SentryLevel.DEBUG);
        return handleExceptionInternal(exception, prepareErrorResponse(exception.getMessage(), null), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    private Map<String, Object> prepareErrorResponse(String message, Map<String, String> validationErrors) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("message", message);

        if (validationErrors != null && !validationErrors.isEmpty()) {
            List<Map<String, String>> validationErrorsList = new ArrayList<>();
            for (Map.Entry<String, String> entry : validationErrors.entrySet()) {
                Map<String, String> validationErrorsMap = new HashMap<>();
                validationErrorsMap.put("field", entry.getKey());
                validationErrorsMap.put("message", entry.getValue());
                validationErrorsList.add(validationErrorsMap);
            }
            errors.put("validationErrors", validationErrorsList);
        }
        return errors;
    }
}
