package com.senpare.examservice.dto;

import com.senpare.examservice.utilities.Util;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class OptionDTO {

    private UUID uuid;

    private UUID questionUuid;

    private String title;

    private boolean selected;

    private String createdOn;

    private String modifiedOn;

    private String createdBy;

    private String modifiedBy;

    public OptionDTO setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public OptionDTO setQuestionUuid(UUID questionUuid) {
        this.questionUuid = questionUuid;
        return this;
    }

    public OptionDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public OptionDTO setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    public OptionDTO setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = Util.toHumanReadableFormat(createdOn);
        return this;
    }

    public OptionDTO setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = Util.toHumanReadableFormat(modifiedOn);
        return this;
    }

    public OptionDTO setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public OptionDTO setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }
}
