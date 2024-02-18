package com.senpare.paymentservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@JsonPropertyOrder({
        "first_name",
        "last_name",
        "email",
        "amount",
        "charge",
        "mode",
        "type",
        "status",
        "reference",
        "created_at",
        "updated_at"
})
@Getter
public class ChapaWebhookRequest {

    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String email;
    private String amount;
    private String charge;
    private String mode;
    private String type;
    private String status;
    private String reference;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;

    public ChapaWebhookRequest setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public ChapaWebhookRequest setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public ChapaWebhookRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public ChapaWebhookRequest setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public ChapaWebhookRequest setCharge(String charge) {
        this.charge = charge;
        return this;
    }

    public ChapaWebhookRequest setMode(String mode) {
        this.mode = mode;
        return this;
    }

    public ChapaWebhookRequest setType(String type) {
        this.type = type;
        return this;
    }

    public ChapaWebhookRequest setStatus(String status) {
        this.status = status;
        return this;
    }

    public ChapaWebhookRequest setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public ChapaWebhookRequest setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ChapaWebhookRequest setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}

