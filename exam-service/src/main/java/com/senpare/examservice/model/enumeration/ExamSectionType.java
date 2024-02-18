package com.senpare.examservice.model.enumeration;

import com.senpare.examservice.exception.BadRequestException;
import com.senpare.examservice.utilities.Util;

public enum ExamSectionType {

    MULTIPLE_CHOICE("Multiple Choice"),
    SHORT_ANSWER("Short Answer");

    private final String displayText;

    ExamSectionType(String displayText) {
        this.displayText = displayText;
    }

    public static ExamSectionType lookupByCode(String examSectionType) {
        if (Util.isNullOrEmpty(examSectionType)) {
            throw new BadRequestException("ExamSectionType can't be null or empty");
        }

        try{
            return ExamSectionType.valueOf(examSectionType.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public String getDisplayText() {
        return displayText;
    }

}
