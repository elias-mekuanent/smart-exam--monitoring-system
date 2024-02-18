package com.senpare.examservice.exception;

public class LicenseViolationException extends RuntimeException {

    public LicenseViolationException(String message) {
        super(message);
    }
}
