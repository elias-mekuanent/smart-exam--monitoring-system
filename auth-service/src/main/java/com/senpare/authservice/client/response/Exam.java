package com.senpare.authservice.client.response;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Exam {

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

    private ExamSetting examSetting;

    private String createdOn;

    private String modifiedOn;

    private String createdBy;

    private String modifiedBy;

    public Exam setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public Exam setLicenseUuid(UUID licenseUuid) {
        this.licenseUuid = licenseUuid;
        return this;
    }

    public Exam setTitle(String title) {
        this.title = title;
        return this;
    }

    public Exam setDescription(String description) {
        this.description = description;
        return this;
    }

    public Exam setExamInstruction(String examInstruction) {
        this.examInstruction = examInstruction;
        return this;
    }

    public Exam setExamType(String examType) {
        this.examType = examType;
        return this;
    }

    public Exam setPlannedStartDateTime(String plannedStartDateTime) {
        this.plannedStartDateTime = plannedStartDateTime;
        return this;
    }

    public Exam setActualStartDateTime(String actualStartDateTime) {
        this.actualStartDateTime = actualStartDateTime;
        return this;
    }

    public Exam setPlannedDuration(String plannedDuration) {
        this.plannedDuration = plannedDuration;
        return this;
    }

    public Exam setActualDuration(String actualDuration) {
        this.actualDuration = actualDuration;
        return this;
    }

    public Exam setLocked(boolean locked) {
        this.locked = locked;
        return this;
    }

    public Exam setExamStatus(String examStatus) {
        this.examStatus = examStatus;
        return this;
    }

    public Exam setExamSetting(ExamSetting examSetting) {
        this.examSetting = examSetting;
        return this;
    }

    public Exam setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public Exam setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
        return this;
    }

    public Exam setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Exam setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }
}
