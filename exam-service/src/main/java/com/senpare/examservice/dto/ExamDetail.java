package com.senpare.examservice.dto;

import com.senpare.examservice.model.enumeration.ExamType;
import com.senpare.examservice.utilities.Util;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
public class ExamDetail {

    private String title;

    private String description;

    private String examInstruction;

    private String examType;

    private String plannedStartDateTime;

    private String plannedDuration;

    public ExamDetail setTitle(String title) {
        this.title = title;
        return this;
    }

    public ExamDetail setDescription(String description) {
        this.description = description;
        return this;
    }

    public ExamDetail setExamInstruction(String examInstruction) {
        this.examInstruction = examInstruction;
        return this;
    }

    public ExamDetail setExamType(ExamType examType) {
        this.examType = examType.getDisplayText();
        return this;
    }

    public ExamDetail setPlannedStartDateTime(LocalDateTime plannedStartDateTime) {
        this.plannedStartDateTime = Util.toHumanReadableFormat(plannedStartDateTime);
        return this;
    }


    public ExamDetail setPlannedDuration(Duration plannedDuration) {
        this.plannedDuration = Util.toHumanReadableFormat(plannedDuration);
        return this;
    }
}
