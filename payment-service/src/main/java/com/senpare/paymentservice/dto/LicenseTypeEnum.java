package com.senpare.paymentservice.dto;

import com.senpare.paymentservice.exception.BadRequestException;
import com.senpare.paymentservice.utilities.Util;

import java.math.BigDecimal;

public enum LicenseTypeEnum {

    TEAM(new BigDecimal("1000")),
    BUSINESS(new BigDecimal("5000")),
    ENTERPRISE(null);

    private final BigDecimal amount;

    LicenseTypeEnum(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public static LicenseTypeEnum lookupByCode(String paymentType) {
        if (Util.isNullOrEmpty(paymentType)) {
            throw new BadRequestException("LicenseType can't be null or empty");
        }

        try{
            return LicenseTypeEnum.valueOf(paymentType.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
