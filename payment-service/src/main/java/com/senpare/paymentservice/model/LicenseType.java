package com.senpare.paymentservice.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "license_type")
@Getter
public class LicenseType extends AuditableBean {

    @Id
    @Column(name = "uuid", nullable = false)
    private UUID uuid;
    @Column(name = "type_name", unique = true, nullable = false)
    private String typeName;
    @Column(unique = true, nullable = false)
    private BigDecimal amount;
    @Column(name = "allowed_examinedd_count", nullable = false)
    private int allowedExamineeCount;
    @Column(name = "camera_security", nullable = false)
    private Boolean cameraSecurity;
    @Column(name = "audio_record_security", nullable = false)
    private Boolean audioRecordSecurity;
    @Column(name = "screenshot_security", nullable = false)
    private Boolean screenshotSecurity;
    @Column(name = "mouse_track_security", nullable = false)
    private Boolean mouseTrackSecurity;

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
        return (LicenseType) super.setCreatedOn(createdOn);
    }

    public LicenseType setCreatedBy(String createdBy) {
        return (LicenseType) super.setCreatedBy(createdBy);
    }

    public LicenseType setModifiedOn(LocalDateTime modifiedOn) {
        return (LicenseType) super.setModifiedOn(modifiedOn);
    }

    public LicenseType setModifiedBy(String modifiedBy) {
        return (LicenseType) super.setModifiedBy(modifiedBy);
    }


}
