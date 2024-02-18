package com.senpare.examservice.client.response;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class LicenseType {

    private UUID uuid;

    private String typeName;

    private BigDecimal amount;

    private int allowedExamineeCount;

    private Boolean cameraSecurity;

    private Boolean audioRecordSecurity;

    private Boolean screenshotSecurity;

    private Boolean mouseTrackSecurity;

    private LocalDateTime createdOn;

    private String createdBy;

    private LocalDateTime modifiedOn;

    private String modifiedBy;

    public LicenseType setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public LicenseType setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public LicenseType setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public LicenseType setAllowedExamineeCount(int allowedExamineeCount) {
        this.allowedExamineeCount = allowedExamineeCount;
        return this;
    }

    public LicenseType setCameraSecurity(boolean cameraSecurity) {
        this.cameraSecurity = cameraSecurity;
        return this;
    }

    public LicenseType setAudioRecordSecurity(boolean audioRecordSecurity) {
        this.audioRecordSecurity = audioRecordSecurity;
        return this;
    }

    public LicenseType setScreenshotSecurity(boolean screenshotSecurity) {
        this.screenshotSecurity = screenshotSecurity;
        return this;
    }

    public LicenseType setMouseTrackSecurity(boolean mouseTrackSecurity) {
        this.mouseTrackSecurity = mouseTrackSecurity;
        return this;
    }

    public LicenseType setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public LicenseType setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LicenseType setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
        return this;
    }

    public LicenseType setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }
}
