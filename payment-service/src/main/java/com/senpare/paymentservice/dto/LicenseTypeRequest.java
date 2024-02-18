package com.senpare.paymentservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class LicenseTypeRequest {

    @NotBlank(message = "Type name can't be null or empty")
    private String typeName;
    @Min(value = 1L, message = "Amount can't be negative")
    @NotNull(message = "Amount can't be null")
    private BigDecimal amount;
    @Min(value = 1L, message = "Allowed examiner count can't be less than 1")
    @NotNull(message = "Allowed examinee count can't be null")
    private Integer allowedExamineeCount;
    @NotNull(message = "Camera security can't be null")
    private Boolean cameraSecurity;
    @NotNull(message = "Audio record security can't be null")
    private Boolean audioRecordSecurity;
    @NotNull(message = "Screenshot security can't be null")
    private Boolean screenshotSecurity;
    @NotNull(message = "Mouse track security can't be null")
    private Boolean mouseTrackSecurity;

    public LicenseTypeRequest setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public LicenseTypeRequest setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public LicenseTypeRequest setAllowedExamineeCount(Integer allowedExamineeCount) {
        this.allowedExamineeCount = allowedExamineeCount;
        return this;
    }

    public LicenseTypeRequest setCameraSecurity(Boolean cameraSecurity) {
        this.cameraSecurity = cameraSecurity;
        return this;
    }

    public LicenseTypeRequest setAudioRecordSecurity(Boolean audioRecordSecurity) {
        this.audioRecordSecurity = audioRecordSecurity;
        return this;
    }

    public LicenseTypeRequest setScreenshotSecurity(Boolean screenshotSecurity) {
        this.screenshotSecurity = screenshotSecurity;
        return this;
    }

    public LicenseTypeRequest setMouseTrackSecurity(Boolean mouseTrackSecurity) {
        this.mouseTrackSecurity = mouseTrackSecurity;
        return this;
    }
}
