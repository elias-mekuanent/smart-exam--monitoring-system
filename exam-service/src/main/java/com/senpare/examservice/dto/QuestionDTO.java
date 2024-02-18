package com.senpare.examservice.dto;

import com.senpare.examservice.model.enumeration.OptionOrderType;
import com.senpare.examservice.utilities.Util;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class QuestionDTO {


    private UUID uuid;

    private UUID examSectionUuid;

    private String title;

    private String optionOrderType;

    private List<OptionDTO> options;

    private String createdOn;

    private String modifiedOn;

    private String createdBy;

    private String modifiedBy;


    public QuestionDTO setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public QuestionDTO setExamSectionUuid(UUID examSectionUuid) {
        this.examSectionUuid = examSectionUuid;
        return this;
    }

    public QuestionDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public QuestionDTO setOptionOrderType(OptionOrderType optionOrderType) {
        this.optionOrderType = optionOrderType.getDisplayText();
        return this;
    }

    public QuestionDTO setOptions(List<OptionDTO> options) {
        this.options = options;
        return this;
    }

    public QuestionDTO setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = Util.toHumanReadableFormat(createdOn);
        return this;
    }

    public QuestionDTO setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn =  Util.toHumanReadableFormat(modifiedOn);
        return this;
    }

    public QuestionDTO setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public QuestionDTO setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }
}
