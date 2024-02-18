package com.senpare.paymentservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class LicenseRequest {

    @NotBlank(message = "Email can't be null or empty")
    private String email;
    @Min(value = 0, message = "Amount can't be negative")
    @NotNull(message = "Amount can't be null")
    private BigDecimal amount;

    public LicenseRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public LicenseRequest setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }
}
