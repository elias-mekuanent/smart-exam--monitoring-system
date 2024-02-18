package com.senpare.paymentservice.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PaymentLogRequest {

    private String email;
    private String paymentProvider;
    private String transactionReference;
    private BigDecimal amount;

    public PaymentLogRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public PaymentLogRequest setPaymentProvider(String paymentProvider) {
        this.paymentProvider = paymentProvider;
        return this;
    }

    public PaymentLogRequest setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
        return this;
    }

    public PaymentLogRequest setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }
}
