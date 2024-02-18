package com.senpare.examservice.client.response;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class License {

    private UUID uuid;

    private String email;

    private UUID licenseKey;

    private LicenseType licenseType;

    private String licenseStatus;

    private LocalDateTime createdOn;

    private String createdBy;

    private LocalDateTime modifiedOn;

    private String modifiedBy;

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

    public License setLicenseStatus(String licenseStatus) {
        this.licenseStatus = licenseStatus;
        return this;
    }

    public License setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public License setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public License setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
        return this;
    }

    public License setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }
}

