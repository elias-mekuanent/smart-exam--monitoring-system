package com.senpare.paymentservice.model;

import com.senpare.paymentservice.dto.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_log")
@Getter
public class PaymentLog extends AuditableBean {

    @Id
    @Column(name = "uuid", nullable = false)
    private UUID uuid;
    @Column(nullable = false)
    private String email;
    @Column(name = "payment_provider", nullable = false)
    private String paymentProvider;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(name = "transaction_reference", unique = true, nullable = false)
    private String transactionReference;
    @OneToOne(targetEntity = License.class, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "license_id", referencedColumnName = "uuid")
    private License license;
    @Enumerated
    @Column(nullable = false)
    private PaymentStatus status;

    public PaymentLog setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public PaymentLog setEmail(String email) {
        this.email = email;
        return this;
    }

    public PaymentLog setPaymentProvider(String paymentProvider) {
        this.paymentProvider = paymentProvider;
        return this;
    }

    public PaymentLog setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public PaymentLog setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
        return this;
    }

    public PaymentLog setLicense(License license) {
        this.license = license;
        return this;
    }

    public PaymentLog setStatus(PaymentStatus status) {
        this.status = status;
        return this;
    }

    public PaymentLog setCreatedOn(LocalDateTime createdOn) {
        return (PaymentLog) super.setCreatedOn(createdOn);
    }

    public PaymentLog setCreatedBy(String createdBy) {
        return (PaymentLog) super.setCreatedBy(createdBy);
    }

    public PaymentLog setModifiedOn(LocalDateTime modifiedOn) {
        return (PaymentLog) super.setModifiedOn(modifiedOn);
    }

    public PaymentLog setModifiedBy(String modifiedBy) {
        return (PaymentLog) super.setModifiedBy(modifiedBy);
    }
}
