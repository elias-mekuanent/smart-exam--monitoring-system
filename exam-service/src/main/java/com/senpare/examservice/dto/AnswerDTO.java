package com.senpare.examservice.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class AnswerDTO {

    private UUID questionUUid;

    private UUID optionUuid;

    public AnswerDTO setQuestionUUid(UUID questionUUid) {
        this.questionUUid = questionUUid;
        return this;
    }

    public AnswerDTO setOptionUuid(UUID optionUuid) {
        this.optionUuid = optionUuid;
        return this;
    }
}
