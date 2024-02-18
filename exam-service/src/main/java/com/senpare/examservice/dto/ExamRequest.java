package com.senpare.examservice.dto;

import com.senpare.examservice.model.enumeration.ExamType;
import com.senpare.examservice.utilities.Util;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ExamRequest {

    private String licenseUuid;

    private String title;

    private String description;

    private String examInstruction;

    private String examType;

    private LocalDateTime plannedStartDateTime;

    private Integer plannedDuration;

    public ExamRequest setLicenseUuid(String licenseUuid) {
        this.licenseUuid = licenseUuid;
        return this;
    }

    public ExamRequest setLicenseUuid(UUID licenseUuid) {
        this.licenseUuid = licenseUuid.toString();
        return this;
    }
    public ExamRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    public ExamRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public ExamRequest setExamInstruction(String examInstruction) {
        this.examInstruction = examInstruction;
        return this;
    }

    public ExamType getExamType() {
        return Util.isNotNullAndEmpty(examType) ? ExamType.lookupByCode(examType) : null;
    }

    public ExamRequest setExamType(String examType) {
        this.examType = examType;
        return this;
    }

//    public ExamRequest setCategory(Category category) {
//        this.category = category;
//        return this;
//    }

    public ExamRequest setPlannedStartDateTime(LocalDateTime plannedStartDateTime) {
        this.plannedStartDateTime = plannedStartDateTime;
        return this;
    }


    public ExamRequest setPlannedDuration(Integer plannedDuration) {
        this.plannedDuration = plannedDuration;
        return this;
    }

}
