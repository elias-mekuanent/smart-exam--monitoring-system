package com.senpare.examservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senpare.examservice.model.enumeration.SecurityStrikeType;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "exam_security_strike")
public class ExamSecurityStrike extends AuditableBean {

    @Id
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "exam_uuid", referencedColumnName = "uuid", nullable = false)
    private Exam exam;

    @Enumerated(EnumType.STRING)
    @Column(name = "strike_type", nullable = false)
    private SecurityStrikeType strikeType;

    @Column(name = "exam_time", nullable = false)
    private Duration examTime;

    public ExamSecurityStrike setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public ExamSecurityStrike setExam(Exam exam) {
        this.exam = exam;
        return this;
    }

    public ExamSecurityStrike setStrikeType(SecurityStrikeType strikeType) {
        this.strikeType = strikeType;
        return this;
    }

    public ExamSecurityStrike setStrikeType(String strikeType) {
        this.strikeType =SecurityStrikeType.lookupByCode(strikeType);
        return this;
    }

    public ExamSecurityStrike setExamTime(Duration examTime) {
        this.examTime = examTime;
        return this;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public Duration getExamTime() {
        return examTime;
    }

    @Override
    public ExamSecurityStrike setCreatedOn(LocalDateTime createdOn) {
        return (ExamSecurityStrike) super.setCreatedOn(createdOn);
    }

    @Override
    public ExamSecurityStrike setCreatedBy(String createdBy) {
        return (ExamSecurityStrike) super.setCreatedBy(createdBy);
    }

    @Override
    public ExamSecurityStrike setModifiedOn(LocalDateTime modifiedOn) {
        return (ExamSecurityStrike) super.setModifiedOn(modifiedOn);
    }

    @Override
    public ExamSecurityStrike setModifiedBy(String modifiedBy) {
        return (ExamSecurityStrike) super.setModifiedBy(modifiedBy);
    }

}
