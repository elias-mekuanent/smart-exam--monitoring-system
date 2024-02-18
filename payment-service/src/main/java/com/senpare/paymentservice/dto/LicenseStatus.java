package com.senpare.paymentservice.dto;

import com.senpare.paymentservice.exception.BadRequestException;
import com.senpare.paymentservice.utilities.Util;

public enum LicenseStatus {
    NEW,
    USED;

    public static LicenseStatus lookupByCode(String licenseStatus) {
        if (Util.isNullOrEmpty(licenseStatus)) {
            throw new BadRequestException("LicenseStatus can't be null or empty");
        }

        try{
            return LicenseStatus.valueOf(licenseStatus.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
