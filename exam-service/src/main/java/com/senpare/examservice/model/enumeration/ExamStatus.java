package com.senpare.examservice.model.enumeration;

import com.senpare.examservice.exception.BadRequestException;
import com.senpare.examservice.utilities.Util;

public enum ExamStatus {

    CREATED("Created"),
    STARTED("Started"),
    COMPLETED("Completed"),
    CANCELED("Canceled");

    private final String displayText;

    ExamStatus(String displayText) {
        this.displayText = displayText;
    }

    public static ExamStatus lookupByCode(String examStatus) {
        if (Util.isNullOrEmpty(examStatus)) {
            throw new BadRequestException("ExamStatus can't be null or empty");
        }

        try{
            return ExamStatus.valueOf(examStatus.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public String getDisplayText() {
        return displayText;
    }
}
