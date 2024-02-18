package com.senpare.examservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.senpare.examservice.model.enumeration.ExamStatus;
import com.senpare.examservice.model.enumeration.ExamType;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Table(name = "exam")
public class Exam extends AuditableBean {

    @Id
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Column(name = "license_uuid", nullable = false)
    private UUID licenseUuid;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "exam_instruction", nullable = false)
    private String examInstruction;

    @Column(name = "exam_type")
    @Enumerated(EnumType.STRING)
    private ExamType examType;

//    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
//    @JoinColumn(name = "category_id", referencedColumnName = "uuid")
//    private Category category;

    @Column(name = "planned_start_datetime")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime plannedStartDateTime;

    @Column(name = "actual_start_datetime")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime actualStartDateTime;

    @Column(name = "planned_duration", nullable = false)
    private Duration plannedDuration;

    @Column(name = "actual_duration")
    private Duration actualDuration;

    private boolean locked;

    @Column(name = "exam_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExamStatus examStatus;

    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "exam_setting_uuid", referencedColumnName = "uuid", unique = true, nullable = false)
    private ExamSetting examSetting;

    @JsonIgnore
    @OneToMany(mappedBy = "exam",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ExamSection> examSection;

    @JsonIgnore
    @OneToMany(mappedBy = "exam",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ExamEnrollment> examEnrollments;

    @JsonIgnore
    @OneToMany(mappedBy = "exam",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ExamSecurityStrike> examSecurityStrikes;

    public Exam setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public Exam setLicenseUuid(UUID licenseUuid) {
        this.licenseUuid = licenseUuid;
        return this;
    }

    public Exam setLicenseUuid(String licenseUuid) {
        this.licenseUuid = UUID.fromString(licenseUuid);
        return this;
    }


    public Exam setTitle(String title) {
        this.title = title;
        return this;
    }

    public Exam setDescription(String description) {
        this.description = description;
        return this;
    }

    public Exam setExamInstruction(String examInstruction) {
        this.examInstruction = examInstruction;
        return this;
    }

    public Exam setExamType(ExamType examType) {
        this.examType = examType;
        return this;
    }

    public Exam setExamType(String examType) {
        this.examType = ExamType.lookupByCode(examType);
        return this;
    }

//    public Exam setCategory(Category category) {
//        this.category = category;
//        return this;
//    }

    public Exam setPlannedStartDateTime(LocalDateTime plannedStartDateTime) {
        this.plannedStartDateTime = plannedStartDateTime;
        return this;
    }

    public LocalDateTime getPlannedEndDateTime() {
        return plannedStartDateTime.plusMinutes(plannedDuration.toMinutes());
    }

    public Exam setActualStartDateTime(LocalDateTime actualStartDateTime) {
        this.actualStartDateTime = actualStartDateTime;
        return this;
    }

    public LocalDateTime getActualEndDateTime() {
        return actualStartDateTime.plusMinutes(actualDuration.toMinutes());
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public Duration getPlannedDuration() {
        return plannedDuration;
    }

    public Exam setPlannedDuration(Duration plannedDuration) {
        this.plannedDuration = plannedDuration;
        return this;
    }

    public Exam setActualDuration(Duration actualDuration) {
        this.actualDuration = actualDuration;
        return this;
    }

    public Exam setLocked(boolean locked) {
        this.locked = locked;
        return this;
    }

    public Exam setExamStatus(ExamStatus examStatus) {
        this.examStatus = examStatus;
        return this;
    }

    public Exam setExamStatus(String examStatus) {
        this.examStatus = ExamStatus.lookupByCode(examStatus);
        return this;
    }

    public Exam setExamSetting(ExamSetting examSetting) {
        this.examSetting = examSetting;
        return this;
    }

    @Override
    public Exam setCreatedOn(LocalDateTime createdOn) {
        return (Exam) super.setCreatedOn(createdOn);
    }

    @Override
    public Exam setCreatedBy(String createdBy) {
        return (Exam) super.setCreatedBy(createdBy);
    }

    @Override
    public Exam setModifiedOn(LocalDateTime modifiedOn) {
        return (Exam) super.setModifiedOn(modifiedOn);
    }

    @Override
    public Exam setModifiedBy(String modifiedBy) {
        return (Exam) super.setModifiedBy(modifiedBy);
    }

}