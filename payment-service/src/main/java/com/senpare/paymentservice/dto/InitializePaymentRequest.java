package com.senpare.paymentservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class InitializePaymentRequest {

    @NotBlank(message = "Payment type can't be null or empty")
    private String paymentType;
    @NotBlank(message = "Currency can't be null or empty")
    private String currency;
    private String returnUrl;

    public InitializePaymentRequest setPaymentType(String paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public InitializePaymentRequest setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public InitializePaymentRequest setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
        return this;
    }
}
