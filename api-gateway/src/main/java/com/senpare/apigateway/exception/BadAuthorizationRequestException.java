package com.senpare.apigateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class BadAuthorizationRequestException extends RuntimeException {

    public BadAuthorizationRequestException(String message) {
        super(message);
    }
}
