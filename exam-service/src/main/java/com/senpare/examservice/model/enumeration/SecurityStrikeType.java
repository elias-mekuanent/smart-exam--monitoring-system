package com.senpare.examservice.model.enumeration;

import com.senpare.examservice.exception.BadRequestException;
import com.senpare.examservice.utilities.Util;

public enum SecurityStrikeType {

    LEAVING_EXAM_PAGE(Severity.HIGH_SEVERITY, 3),
    BACKGROUND_NOISE(Severity.MEDIUM_SEVERITY, 3),
    MULTIPLE_PEOPLE_ON_SCREEN(Severity.HIGH_SEVERITY, 1),
    NETWORK_ISSUES(Severity.LOW_SEVERITY, 5);

    private final Severity severity;

    private final int maxStrikeCount;

    SecurityStrikeType(Severity severity, int maxStrikeCount) {
        this.severity = severity;
        this.maxStrikeCount = maxStrikeCount;
    }

    public static SecurityStrikeType lookupByCode(String securityStrikeType) {
        if (Util.isNullOrEmpty(securityStrikeType)) {
            throw new BadRequestException("SecurityStrikeType can't be null or empty");
        }

        try{
            return SecurityStrikeType.valueOf(securityStrikeType.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public Severity getSeverity() {
        return severity;
    }

    public int getMaxStrikeCount() {
        return maxStrikeCount;
    }

}
