package com.senpare.authservice.model.enumeration;

import com.senpare.authservice.exception.BadRequestException;
import com.senpare.authservice.utilities.Util;

public enum ExaminerAccountType {

    PERSONAL("Personal"),
    COMPANY("Company");

    private final String displayText;

    ExaminerAccountType(String displayText) {
        this.displayText = displayText;
    }

    public static ExaminerAccountType lookupByCode(String examinerAccountType) {
        if (Util.isNullOrEmpty(examinerAccountType)) {
            throw new BadRequestException("ExaminerAccountType can't be null or empty");
        }

        try{
            return ExaminerAccountType.valueOf(examinerAccountType.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public String getDisplayText() {
        return displayText;
    }
}
