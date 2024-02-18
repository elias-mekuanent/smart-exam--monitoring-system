package com.senpare.paymentservice.dto;

import lombok.Getter;

@Getter
public class InitializePaymentResponse {

    private String checkoutUrl;
    private String txRef;
    private String message;

    public InitializePaymentResponse setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
        return this;
    }

    public InitializePaymentResponse setTxRef(String txRef) {
        this.txRef = txRef;
        return this;
    }

    public InitializePaymentResponse setMessage(String message) {
        this.message = message;
        return this;
    }
}
