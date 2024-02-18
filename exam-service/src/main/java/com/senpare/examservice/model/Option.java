package com.senpare.examservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "option")
public class Option extends AuditableBean {

    @Id
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "question_uuid", referencedColumnName = "uuid", nullable = false)
    private Question question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(name = "correct_option")
    private boolean correctOption;

    @JsonIgnore
    @OneToOne(mappedBy = "option", cascade = CascadeType.ALL, orphanRemoval = true)
    private Answer answer;


    public Option setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public Option setQuestion(Question question) {
        this.question = question;
        return this;
    }

    public Option setTitle(String title) {
        this.title = title;
        return this;
    }

    public Option setCorrectOption(boolean correctOption) {
        this.correctOption = correctOption;
        return this;
    }

    @Override
    public Option setCreatedOn(LocalDateTime createdOn) {
        return (Option) super.setCreatedOn(createdOn);
    }

    @Override
    public Option setCreatedBy(String createdBy) {
        return (Option) super.setCreatedBy(createdBy);
    }

    @Override
    public Option setModifiedOn(LocalDateTime modifiedOn) {
        return (Option) super.setModifiedOn(modifiedOn);
    }

    @Override
    public Option setModifiedBy(String modifiedBy) {
        return (Option) super.setModifiedBy(modifiedBy);
    }
}