package com.senpare.examservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.senpare.examservice.model.enumeration.ExamSectionType;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Table(name = "exam_section")
public class ExamSection extends AuditableBean {

    @Id
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "exam_id", referencedColumnName = "uuid", nullable = false)
    private Exam exam;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(name = "question_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExamSectionType examSectionType;

    @Column(name = "weight_per_question", nullable = false)
    private double weightPerQuestion;

    @JsonIgnore
    @OneToMany(mappedBy = "examSection",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Question> questions;

    public ExamSection setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public ExamSection setExam(Exam exam) {
        this.exam = exam;
        return this;
    }

    public ExamSection setTitle(String title) {
        this.title = title;
        return this;
    }

    public ExamSection setExamSectionType(ExamSectionType examSectionType) {
        this.examSectionType = examSectionType;
        return this;
    }

    public ExamSection setExamSectionType(String examSectionType) {
        this.examSectionType = ExamSectionType.lookupByCode(examSectionType);
        return this;
    }

    public ExamSection setWeightPerQuestion(double weightPerQuestion) {
        this.weightPerQuestion = weightPerQuestion;
        return this;
    }

    @Override
    public ExamSection setCreatedOn(LocalDateTime createdOn) {
        return (ExamSection) super.setCreatedOn(createdOn);
    }

    @Override
    public ExamSection setCreatedBy(String createdBy) {
        return (ExamSection) super.setCreatedBy(createdBy);
    }

    @Override
    public ExamSection setModifiedOn(LocalDateTime modifiedOn) {
        return (ExamSection) super.setModifiedOn(modifiedOn);
    }

    @Override
    public ExamSection setModifiedBy(String modifiedBy) {
        return (ExamSection) super.setModifiedBy(modifiedBy);
    }
}
