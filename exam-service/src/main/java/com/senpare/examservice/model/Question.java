package com.senpare.examservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.senpare.examservice.model.enumeration.OptionOrderType;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Table(name = "question")
public class Question extends AuditableBean {

    @Id
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "exam_section_id", referencedColumnName = "uuid", nullable = false)
    private ExamSection examSection;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(name = "option_order_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OptionOrderType optionOrderType;

    @JsonIgnore
    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Option> options;

    public Question setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public Question setExamSection(ExamSection examSection) {
        this.examSection = examSection;
        return this;
    }

    public Question setTitle(String title) {
        this.title = title;
        return this;
    }

    public Question setOptionOrderType(OptionOrderType optionOrderType) {
        this.optionOrderType = optionOrderType;
        return this;
    }

    public Question setOptionOrderType(String optionOrderType) {
        this.optionOrderType = OptionOrderType.lookupByCode(optionOrderType);
        return this;
    }

    @Override
    public Question setCreatedOn(LocalDateTime createdOn) {
        return (Question) super.setCreatedOn(createdOn);
    }

    @Override
    public Question setCreatedBy(String createdBy) {
        return (Question) super.setCreatedBy(createdBy);
    }

    @Override
    public Question setModifiedOn(LocalDateTime modifiedOn) {
        return (Question) super.setModifiedOn(modifiedOn);
    }

    @Override
    public Question setModifiedBy(String modifiedBy) {
        return (Question) super.setModifiedBy(modifiedBy);
    }
}
