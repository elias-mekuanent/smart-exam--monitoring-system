package com.senpare.authservice.utilities;

public class AuthServiceMessages {

    public static String canNotBeNull(String resourceName) {
        return String.format("%s can't be null", resourceName);
    }

    public static String canNotBeNullOrEmpty(String fieldName) {
        return String.format("%s can't be null or empty", fieldName);
    }

    public static String canNotBeFound(String resourceName, String identifierName, String identifier) {
        return String.format("%s can't be found by %s: %s", resourceName, identifierName, identifier);
    }

    public static String canNotBeFound(String resourceName, String identifierName) {
        return String.format("%s can't be found by %s", resourceName, identifierName);
    }

    public static String alreadyExists(String resourceName, String identifierName, String identifier) {
        return String.format("%s already exists by %s: %s", resourceName, identifierName, identifier);
    }

    public static String alreadyExists(String resourceName) {
        return String.format("%s already exists", resourceName);
    }

    public static String isIncorrect(String fieldName) {
        return String.format("%s is incorrect", fieldName);
    }

    public static String alreadyConfirmed(String resourceName) {
        return String.format("%s already confirmed", resourceName);
    }

    public static String alreadyExpired(String resourceName) {
        return String.format("%s already expired", resourceName);
    }

    public static String invalidCredentials() {
        return "Invalid username or password";
    }

    public static String exceedsMaxExamineeCount(int allowedCount) {
        return String.format("Exceeds max examinee count(%d) allowed for this exam", allowedCount);
    }

    public static String canNotAddExamineeToExamWithThisStatus(String examStatus) {
        return String.format("Can't add examinee to an exam with status: %s", examStatus);
    }

}
