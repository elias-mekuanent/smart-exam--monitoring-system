package com.senpare.examservice.dto;

import lombok.Getter;

@Getter
public class QuestionRequest {

    private String title;

    private String optionOrderType;

    public QuestionRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    public QuestionRequest setOptionOrderType(String optionOrderType) {
        this.optionOrderType = optionOrderType;
        return this;
    }
}
