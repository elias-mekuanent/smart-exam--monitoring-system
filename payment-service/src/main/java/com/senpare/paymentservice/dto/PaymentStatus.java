package com.senpare.paymentservice.dto;

import com.senpare.paymentservice.exception.BadRequestException;
import com.senpare.paymentservice.utilities.Util;

public enum PaymentStatus {

    INITIALIZED,
    SUCCESSFUL,
    FAILED;

    public static PaymentStatus lookupByCode(String paymentStatus) {
        if (Util.isNullOrEmpty(paymentStatus)) {
            throw new BadRequestException("PaymentStatus can't be null or empty");
        }

        try{
            return PaymentStatus.valueOf(paymentStatus.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
