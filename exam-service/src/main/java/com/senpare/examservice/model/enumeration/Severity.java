package com.senpare.examservice.model.enumeration;

import com.senpare.examservice.exception.BadRequestException;
import com.senpare.examservice.utilities.Util;

public enum Severity {

    HIGH_SEVERITY("High Severity"),
    MEDIUM_SEVERITY("Medium Severity"),
    LOW_SEVERITY("Low Severity");

    private String displayText;

    Severity(String displayText) {
        this.displayText = displayText;
    }

    public static Severity lookupByCode(String severity) {
        if (Util.isNullOrEmpty(severity)) {
            throw new BadRequestException("Severity can't be null or empty");
        }

        try{
            return Severity.valueOf(severity.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public String getDisplayText() {
        return displayText;
    }


}
