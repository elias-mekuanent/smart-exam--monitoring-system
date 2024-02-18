package com.senpare.examservice.model.enumeration;

import com.senpare.examservice.exception.BadRequestException;
import com.senpare.examservice.utilities.Util;

public enum OptionOrderType {

    NUMERIC("Numeric"),
    ALPHABETIC("Alphabetic"),
    ITALIC("Italic");

    private final String displayText;

    OptionOrderType(String displayText) {
        this.displayText = displayText;
    }

    public static OptionOrderType lookupByCode(String optionOrderType) {
        if (Util.isNullOrEmpty(optionOrderType)) {
            throw new BadRequestException("OptionOrderType can't be null or empty");
        }

        try{
            return OptionOrderType.valueOf(optionOrderType.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public String getDisplayText() {
        return displayText;
    }

}
