package com.senpare.examservice.dto;

import lombok.Getter;

@Getter
public class ExamSettingRequest {

    private Integer examineeCount;

    private Boolean includeMockExam;

    private Boolean includeCameraSecurity;

    private Boolean includeVoiceRecordSecurity;

    private Boolean includeScreenSecurity;

    private Boolean includeMouseTrackSecurity;

    private Integer maxSecurityStrike;

    private Boolean autoGrade;

    private Boolean shuffleQuestion;

    public ExamSettingRequest setExamineeCount(Integer examineeCount) {
        this.examineeCount = examineeCount;
        return this;
    }

    public ExamSettingRequest setIncludeMockExam(Boolean includeMockExam) {
        this.includeMockExam = includeMockExam;
        return this;
    }

    public ExamSettingRequest setIncludeCameraSecurity(Boolean includeCameraSecurity) {
        this.includeCameraSecurity = includeCameraSecurity;
        return this;
    }

    public ExamSettingRequest setIncludeVoiceRecordSecurity(Boolean includeVoiceRecordSecurity) {
        this.includeVoiceRecordSecurity = includeVoiceRecordSecurity;
        return this;
    }

    public ExamSettingRequest setIncludeScreenSecurity(Boolean includeScreenSecurity) {
        this.includeScreenSecurity = includeScreenSecurity;
        return this;
    }

    public ExamSettingRequest setIncludeMouseTrackSecurity(Boolean includeMouseTrackSecurity) {
        this.includeMouseTrackSecurity = includeMouseTrackSecurity;
        return this;
    }

    public ExamSettingRequest setMaxSecurityStrike(Integer maxSecurityStrike) {
        this.maxSecurityStrike = maxSecurityStrike;
        return this;
    }

    public ExamSettingRequest setAutoGrade(Boolean autoGrade) {
        this.autoGrade = autoGrade;
        return this;
    }

    public ExamSettingRequest setShuffleQuestion(Boolean shuffleQuestion) {
        this.shuffleQuestion = shuffleQuestion;
        return this;
    }
}
