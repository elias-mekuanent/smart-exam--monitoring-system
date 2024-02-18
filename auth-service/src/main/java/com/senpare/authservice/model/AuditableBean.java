package com.senpare.authservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
@Getter
public abstract class AuditableBean {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(name = "created_on", nullable = false, updatable = false)
    private LocalDateTime createdOn;
    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(name = "modified_on", nullable = false)
    private LocalDateTime modifiedOn;
    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;

    public AuditableBean setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public AuditableBean setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public AuditableBean setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
        return this;
    }

    public AuditableBean setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }
}
