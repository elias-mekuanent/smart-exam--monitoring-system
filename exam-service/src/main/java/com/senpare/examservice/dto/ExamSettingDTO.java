package com.senpare.examservice.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ExamSettingDTO {

    private UUID uuid;

    private boolean includeMockExam;

    private boolean includeCameraSecurity;

    private boolean includeVoiceRecordSecurity;

    private boolean includeScreenSecurity;

    private boolean includeMouseTrackSecurity;

    private int maxSecurityStrike;

    private boolean autoGrade;

    private boolean shuffleQuestion;

    private int examineeCount;


    public ExamSettingDTO setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public ExamSettingDTO setIncludeMockExam(boolean includeMockExam) {
        this.includeMockExam = includeMockExam;
        return this;
    }

    public ExamSettingDTO setIncludeCameraSecurity(boolean includeCameraSecurity) {
        this.includeCameraSecurity = includeCameraSecurity;
        return this;
    }

    public ExamSettingDTO setIncludeVoiceRecordSecurity(boolean includeVoiceRecordSecurity) {
        this.includeVoiceRecordSecurity = includeVoiceRecordSecurity;
        return this;
    }

    public ExamSettingDTO setIncludeScreenSecurity(boolean includeScreenSecurity) {
        this.includeScreenSecurity = includeScreenSecurity;
        return this;
    }

    public ExamSettingDTO setIncludeMouseTrackSecurity(boolean includeMouseTrackSecurity) {
        this.includeMouseTrackSecurity = includeMouseTrackSecurity;
        return this;
    }

    public ExamSettingDTO setMaxSecurityStrike(int maxSecurityStrike) {
        this.maxSecurityStrike = maxSecurityStrike;
        return this;
    }

    public ExamSettingDTO setAutoGrade(boolean autoGrade) {
        this.autoGrade = autoGrade;
        return this;
    }

    public ExamSettingDTO setShuffleQuestion(boolean shuffleQuestion) {
        this.shuffleQuestion = shuffleQuestion;
        return this;
    }

    public ExamSettingDTO setExamineeCount(int examineeCount) {
        this.examineeCount = examineeCount;
        return this;
    }
}
