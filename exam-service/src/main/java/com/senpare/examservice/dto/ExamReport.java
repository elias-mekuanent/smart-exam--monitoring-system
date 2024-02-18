package com.senpare.examservice.dto;

import com.senpare.examservice.model.ExamSecurityStrike;
import com.senpare.examservice.model.enumeration.AttendanceStatus;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class ExamReport {

    private UUID examUuid;

    private String examinee;

    private double totalScore;

    private AttendanceStatus attendanceStatus;

    private List<ExamSecurityStrikeDTO> securityStrikes;


    public ExamReport setExamUuid(UUID examUuid) {
        this.examUuid = examUuid;
        return this;
    }


    public ExamReport setExaminee(String examinee) {
        this.examinee = examinee;
        return this;
    }

    public ExamReport setTotalScore(double totalScore) {
        this.totalScore = totalScore;
        return this;
    }

    public ExamReport setAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
        return this;
    }

    public ExamReport setSecurityStrikes(List<ExamSecurityStrikeDTO> securityStrikes) {
        this.securityStrikes = securityStrikes;
        return this;
    }
}
