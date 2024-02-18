package com.senpare.paymentservice.model;

import com.senpare.paymentservice.dto.LicenseStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "license")
@Getter
public class License extends AuditableBean {

    @Id
    @Column(name = "uuid", nullable = false)
    private UUID uuid;
    @Column(nullable = false)
    private String email;
    @Column(name = "license_key", unique = true, nullable = false)
    private UUID licenseKey;
    @ManyToOne(targetEntity = LicenseType.class, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "license_type_id", referencedColumnName = "uuid", nullable = false)
    private LicenseType licenseType;
    @Enumerated
    @Column(name = "license_status", nullable = false)
    private LicenseStatus licenseStatus;

    public License setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public License setEmail(String email) {
        this.email = email;
        return this;
    }

    public License setLicenseKey(UUID licenseKey) {
        this.licenseKey = licenseKey;
        return this;
    }

    public License setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
        return this;
    }

    public License setLicenseStatus(LicenseStatus licenseStatus) {
        this.licenseStatus = licenseStatus;
        return this;
    }

    public License setCreatedOn(LocalDateTime createdOn) {
        return (License) super.setCreatedOn(createdOn);
    }

    public License setCreatedBy(String createdBy) {
        return (License) super.setCreatedBy(createdBy);
    }

    public License setModifiedOn(LocalDateTime modifiedOn) {
        return (License) super.setModifiedOn(modifiedOn);
    }

    public License setModifiedBy(String modifiedBy) {
        return (License) super.setModifiedBy(modifiedBy);
    }
}
