package com.senpare.paymentservice.dto;

import com.senpare.paymentservice.exception.BadRequestException;
import com.senpare.paymentservice.utilities.Util;

public enum SupportedCurrency {

    USD,
    ETB;

    public static SupportedCurrency lookupByCode(String currency) {
        if (Util.isNullOrEmpty(currency)) {
            throw new BadRequestException("Currency can't be null or empty");
        }

        try{
            return SupportedCurrency.valueOf(currency.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
