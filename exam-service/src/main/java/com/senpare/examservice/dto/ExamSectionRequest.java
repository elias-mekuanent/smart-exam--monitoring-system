package com.senpare.examservice.dto;

import lombok.Getter;

@Getter
public class ExamSectionRequest {

    private String title;

    private String examSectionType;

    private Double weightPerQuestion;

    public ExamSectionRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    public ExamSectionRequest setExamSectionType(String examSectionType) {
        this.examSectionType = examSectionType;
        return this;
    }

    public ExamSectionRequest setWeightPerQuestion(Double weightPerQuestion) {
        this.weightPerQuestion = weightPerQuestion;
        return this;
    }
}
