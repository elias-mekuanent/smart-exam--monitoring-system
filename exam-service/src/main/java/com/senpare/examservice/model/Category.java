package com.senpare.examservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "category")
public class Category extends AuditableBean {

    @Id
    @Column(name = "uuid", nullable = false)
    private UUID uuid;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;

    public Category setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public Category setTitle(String title) {
        this.title = title;
        return this;
    }

    public Category setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public Category setCreatedOn(LocalDateTime createdOn) {
        return (Category) super.setCreatedOn(createdOn);
    }

    @Override
    public Category setCreatedBy(String createdBy) {
        return (Category) super.setCreatedBy(createdBy);
    }

    @Override
    public Category setModifiedOn(LocalDateTime modifiedOn) {
        return (Category) super.setModifiedOn(modifiedOn);
    }

    @Override
    public Category setModifiedBy(String modifiedBy) {
        return (Category) super.setModifiedBy(modifiedBy);
    }
}