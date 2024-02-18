package com.senpare.examservice.dto;

import com.senpare.examservice.model.enumeration.SecurityStrikeType;
import lombok.Getter;

import java.time.Duration;

@Getter
public class ExamSecurityStrikeDTO {


    private SecurityStrikeType strikeType;

    private String examTime;

    private String examineeEmail;

    public ExamSecurityStrikeDTO setStrikeType(SecurityStrikeType strikeType) {
        this.strikeType = strikeType;
        return this;
    }

    public ExamSecurityStrikeDTO setExamTime(String  examTime) {
        this.examTime = examTime;
        return this;
    }

    public ExamSecurityStrikeDTO setExamineeEmail(String examineeEmail) {
        this.examineeEmail = examineeEmail;
        return this;
    }
}
