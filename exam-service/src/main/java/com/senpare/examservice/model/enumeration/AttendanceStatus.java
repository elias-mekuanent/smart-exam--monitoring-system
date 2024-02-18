package com.senpare.examservice.model.enumeration;

import com.senpare.examservice.exception.BadRequestException;
import com.senpare.examservice.utilities.Util;

public enum AttendanceStatus {

    ADDED("Added"),
    PRESENT("Present"),
    ABSENT("Absent"),
    SUSPENDED("Suspended"),
    COMPLETED("Completed");

    private String displayText;

    AttendanceStatus(String displayText) {
        this.displayText = displayText;
    }

    public static AttendanceStatus lookupByCode(String attendanceStatus) {
        if (Util.isNullOrEmpty(attendanceStatus)) {
            throw new BadRequestException("AttendanceStatus can't be null or empty");
        }

        try{
            return AttendanceStatus.valueOf(attendanceStatus.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public String getDisplayText() {
        return displayText;
    }
}
