package com.senpare.examservice.dto;

import lombok.Getter;

@Getter
public class OptionRequest {

    private String title;

    private Boolean correctOption;

    public OptionRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    public OptionRequest setCorrectOption(Boolean correctOption) {
        this.correctOption = correctOption;
        return this;
    }
}
