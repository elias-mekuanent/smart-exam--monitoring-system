package com.senpare.authservice.client.response;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ExamSetting {

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

    public ExamSetting setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public ExamSetting setIncludeMockExam(boolean includeMockExam) {
        this.includeMockExam = includeMockExam;
        return this;
    }

    public ExamSetting setIncludeCameraSecurity(boolean includeCameraSecurity) {
        this.includeCameraSecurity = includeCameraSecurity;
        return this;
    }

    public ExamSetting setIncludeVoiceRecordSecurity(boolean includeVoiceRecordSecurity) {
        this.includeVoiceRecordSecurity = includeVoiceRecordSecurity;
        return this;
    }

    public ExamSetting setIncludeScreenSecurity(boolean includeScreenSecurity) {
        this.includeScreenSecurity = includeScreenSecurity;
        return this;
    }

    public ExamSetting setIncludeMouseTrackSecurity(boolean includeMouseTrackSecurity) {
        this.includeMouseTrackSecurity = includeMouseTrackSecurity;
        return this;
    }

    public ExamSetting setMaxSecurityStrike(int maxSecurityStrike) {
        this.maxSecurityStrike = maxSecurityStrike;
        return this;
    }

    public ExamSetting setAutoGrade(boolean autoGrade) {
        this.autoGrade = autoGrade;
        return this;
    }

    public ExamSetting setShuffleQuestion(boolean shuffleQuestion) {
        this.shuffleQuestion = shuffleQuestion;
        return this;
    }

    public ExamSetting setExamineeCount(int examineeCount) {
        this.examineeCount = examineeCount;
        return this;
    }
}
