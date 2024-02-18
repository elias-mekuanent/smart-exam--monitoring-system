package com.senpare.examservice.dto;

import com.senpare.examservice.model.enumeration.ExamSectionType;
import com.senpare.examservice.utilities.Util;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ExamSectionDTO {

    private UUID uuid;

    private UUID examUuid;

    private String title;

    private String examSectionType;

    private double weightPerQuestion;

    private String createdOn;

    private String modifiedOn;

    private String createdBy;

    private String modifiedBy;

    public ExamSectionDTO setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public ExamSectionDTO setExamUuid(UUID examUuid) {
        this.examUuid = examUuid;
        return this;
    }

    public ExamSectionDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public ExamSectionDTO setExamSectionType(ExamSectionType examSectionType) {
        this.examSectionType = examSectionType.getDisplayText();
        return this;
    }

    public ExamSectionDTO setWeightPerQuestion(double weightPerQuestion) {
        this.weightPerQuestion = weightPerQuestion;
        return this;
    }

    public ExamSectionDTO setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = Util.toHumanReadableFormat(createdOn);
        return this;
    }

    public ExamSectionDTO setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn =  Util.toHumanReadableFormat(modifiedOn);
        return this;
    }

    public ExamSectionDTO setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ExamSectionDTO setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }
}
