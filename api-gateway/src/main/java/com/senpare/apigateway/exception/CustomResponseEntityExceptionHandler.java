package com.senpare.apigateway.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = BadAuthorizationRequestException.class)
    public Mono<ResponseEntity<Object>> handleBadAuthorizationRequests(BadAuthorizationRequestException exception, ServerWebExchange exchange) {
        log.debug(exception.getMessage());
//        Sentry.captureMessage(exception.getMessage(), SentryLevel.DEBUG);
        return handleExceptionInternal(exception, prepareErrorResponse( exception.getMessage(), null), new HttpHeaders(), HttpStatus.UNAUTHORIZED, exchange);
    }

    private Map<String, Object> prepareErrorResponse(String message, Map<String, String> validationErrors) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("message", message);

        if(validationErrors != null && !validationErrors.isEmpty()) {
            List<Map<String, String>> validationErrorsList = new ArrayList<>();
            for(Map.Entry<String, String> entry : validationErrors.entrySet()) {
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
