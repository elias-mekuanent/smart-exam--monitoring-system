package com.senpare.examservice.model.enumeration;

import com.senpare.examservice.exception.BadRequestException;
import com.senpare.examservice.utilities.Util;

public enum QuestionType {

    MULTIPLE_ANSWER("Multiple Answer"),
    SINGLE_ANSWER("Single Answer");

    private final String displayText;

    QuestionType(String displayText) {
        this.displayText = displayText;
    }

    public static QuestionType lookupByCode(String questionType) {
        if (Util.isNullOrEmpty(questionType)) {
            throw new BadRequestException("QuestionType can't be null or empty");
        }

        try{
            return QuestionType.valueOf(questionType.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public String getDisplayText() {
        return displayText;
    }

}
