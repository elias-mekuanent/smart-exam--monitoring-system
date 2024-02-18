package com.senpare.examservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.senpare.examservice.model.enumeration.ExamStatus;
import com.senpare.examservice.model.enumeration.ExamType;
import com.senpare.examservice.utilities.Util;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ExamDTO {

    private UUID uuid;

    private UUID licenseUuid;

    private String title;

    private String description;

    private String examInstruction;

    private String examType;

    private String plannedStartDateTime;

    private String actualStartDateTime;

    private String plannedDuration;

    private String actualDuration;

    private boolean locked;

    private String examStatus;

    @JsonProperty("examSetting")
    private ExamSettingDTO examSettingDTO;

    private String createdOn;

    private String modifiedOn;

    private String createdBy;

    private String modifiedBy;


    public ExamDTO setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public ExamDTO setLicenseUuid(UUID licenseUuid) {
        this.licenseUuid = licenseUuid;
        return this;
    }

    public ExamDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public ExamDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public ExamDTO setExamInstruction(String examInstruction) {
        this.examInstruction = examInstruction;
        return this;
    }

    public ExamDTO setExamType(ExamType examType) {
        this.examType = examType.getDisplayText();
        return this;
    }

    public ExamDTO setPlannedStartDateTime(LocalDateTime plannedStartDateTime) {
        this.plannedStartDateTime = Util.toHumanReadableFormat(plannedStartDateTime);
        return this;
    }

    public ExamDTO setActualStartDateTime(LocalDateTime actualStartDateTime) {
        this.actualStartDateTime = Util.toHumanReadableFormat(actualStartDateTime);
        return this;
    }

    public ExamDTO setPlannedDuration(Duration plannedDuration) {
        this.plannedDuration = Util.toHumanReadableFormat(plannedDuration);
        return this;
    }

    public ExamDTO setActualDuration(Duration actualDuration) {
        this.actualDuration = Util.toHumanReadableFormat(actualDuration);
        return this;
    }

    public ExamDTO setLocked(boolean locked) {
        this.locked = locked;
        return this;
    }

    public ExamDTO setExamStatus(ExamStatus examStatus) {
        this.examStatus = examStatus.getDisplayText();
        return this;
    }

    public ExamDTO setExamSettingDTO(ExamSettingDTO examSettingDTO) {
        this.examSettingDTO = examSettingDTO;
        return this;
    }

    public ExamDTO setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = Util.toHumanReadableFormat(createdOn);
        return this;
    }

    public ExamDTO setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = Util.toHumanReadableFormat(modifiedOn);
        return this;
    }

    public ExamDTO setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ExamDTO setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }
}
