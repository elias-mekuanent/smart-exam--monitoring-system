package com.senpare.examservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "exam_setting")
public class ExamSetting extends AuditableBean {

    @Id
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @JsonIgnore
    @OneToOne(mappedBy = "examSetting",
            cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.LAZY, optional = false
    )
    private Exam exam;

    @Column(name = "include_mock_exam", nullable = false)
    private boolean includeMockExam;

    @Column(name = "include_camera_security", nullable = false)
    private boolean includeCameraSecurity;

    @Column(name = "include_voice_record_security", nullable = false)
    private boolean includeVoiceRecordSecurity;

    @Column(name = "include_screen_security", nullable = false)
    private boolean includeScreenSecurity;

    @Column(name = "include_mouse_track_security", nullable = false)
    private boolean includeMouseTrackSecurity;

    @Column(name = "max_security_strike", nullable = false)
    private int maxSecurityStrike;

    @Column(name = "auto_grade", nullable = false)
    private boolean autoGrade;

    @Column(name = "shuffle_question", nullable = false)
    private boolean shuffleQuestion;

    @Column(name = "examinee_count")
    private int examineeCount;

    public ExamSetting setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public ExamSetting setExam(Exam exam) {
        this.exam = exam;
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

    @Override
    public ExamSetting setCreatedOn(LocalDateTime createdOn) {
        return (ExamSetting) super.setCreatedOn(createdOn);
    }

    @Override
    public ExamSetting setCreatedBy(String createdBy) {
        return (ExamSetting) super.setCreatedBy(createdBy);
    }

    @Override
    public ExamSetting setModifiedOn(LocalDateTime modifiedOn) {
        return (ExamSetting) super.setModifiedOn(modifiedOn);
    }

    @Override
    public ExamSetting setModifiedBy(String modifiedBy) {
        return (ExamSetting) super.setModifiedBy(modifiedBy);
    }
}
