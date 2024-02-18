package com.senpare.examservice.utilities;

import com.senpare.examservice.model.enumeration.ExamStatus;

import java.util.Collection;
import java.util.Collections;

public class ExamServiceMessages {

    public static String canNotBeNull(String resourceName) {
        return String.format("%s can't be null", resourceName);
    }

    public static String canNotBeNullOrEmpty(String fieldName) {
        return String.format("%s can't be null or empty", fieldName);
    }

    public static String canNotBeFound(String resourceName, String identifierName) {
        return String.format("%s can't be found by %s", resourceName, identifierName);
    }

    public static String canNotBeFound(String resourceName, String identifierName, String identifier) {
        return String.format("%s can't be found by %s: %s", resourceName, identifierName, identifier);
    }

    public static String alreadyExists(String resourceName, String identifierName, String identifier) {
        return String.format("%s already exists by %s: %s", resourceName, identifierName, identifier);
    }

    public static String alreadyEnrolled() {
        return "Examinee already enrolled with this account";
    }

    public static String isNotEnrolled() {
        return "Examinee is not enrolled to the exam";
    }

    public static String enrollmentNotActive() {
        return "Examinee enrollment is not active";
    }

    public static String actionNotAllowedInThisLicense(String actionName) {
        return String.format("%s is not allowed in this license", actionName);
    }

    public static String examineeCountExceedsLicenseLimit(int allowedExamineeCount) {
        return String.format("Examinee count exceeds license limit: %d", allowedExamineeCount);
    }

    public static String invalidLicense() {
        return "License is used or does not exist";
    }

    public static String canNotDeleteExamWithThisStatus(ExamStatus examStatus) {
        return String.format("Exam can't be deleted when exam status is %s", examStatus);
    }

    public static String canNotStartExamWithThisStatus(ExamStatus examStatus) {
        return String.format("Exam can't be started when exam status is %s", examStatus);
    }

    public static String canNotCompleteExamWithThisStatus(ExamStatus examStatus) {
        return String.format("Exam can't be completed when exam status is: %s", examStatus);
    }

    public static String canNotUpdateExamToThisStatus(ExamStatus examStatus) {
        return String.format("Exam status can't be updated to this status: %s", examStatus);
    }

    public static String examAlreadyStarted() {
        return String.format("");
    }

    public static String canNotDuplicateResource(String resourceName, String resourceContainer) {
        return String.format("Can't add multiple %s in one %s", resourceName, resourceContainer);
    }

    public static String canNotFetchResourceDueToExamStatus(String resourceName, ExamStatus examStatus) {
        return String.format("%ss can't be fetched if exam status is %s", resourceName, examStatus);
    }

    public static String canNotPerformActionWhileExamIsOnThisStatus(String action, ExamStatus examStatus) {
        return String.format("Can't %s while exam is on this status: %s", action, examStatus);
    }
}
