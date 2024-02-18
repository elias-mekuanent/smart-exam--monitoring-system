package com.senpare.examservice.model.enumeration;

import com.senpare.examservice.exception.BadRequestException;
import com.senpare.examservice.utilities.Util;

public enum ExamType {

    PRIVATE_OPEN_BOOK("Private Open Book"),
    PUBLIC_OPEN_BOOK("Public Open Book"),
    PRIVATE_CLOSED_BOOK("Private Closed Book"),
    PUBLIC_CLOSED_BOOK("Public Closed Book");

//    SPECIFIED_PERIOD,
//    TIME_LIMTLESS

    private final String displayText;

    ExamType(String displayText) {
        this.displayText = displayText;
    }

    public static ExamType lookupByCode(String examType) {
        if (Util.isNullOrEmpty(examType)) {
            throw new BadRequestException("ExamType can't be null or empty");
        }

        try{
            return ExamType.valueOf(examType.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public String getDisplayText() {
        return displayText;
    }
}
