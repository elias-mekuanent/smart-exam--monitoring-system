package com.senpare.authservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "confirmation_token")
@Getter
public class ConfirmationToken {


    @Id
    @Column(name = "confirmation_toke_id", nullable = false)
    private UUID uuid;
    @Column(nullable = false, unique = true)
    private String token;
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;
    @Column(name = "modified_at")
    @LastModifiedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime modifiedAt;
    @Future
    @Column(name = "expires_at", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime expiresAt;
    @PastOrPresent
    @Column(name = "confirmed_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime confirmedAt;
    @OneToOne
    @JoinColumn(
            nullable = false,
            name = "appuser_uuid",
            referencedColumnName = "uuid",
            unique = true
    )
    private AppUser appUser;

    public ConfirmationToken setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public ConfirmationToken setToken(String token) {
        this.token = token;
        return this;
    }

    public ConfirmationToken setCreatedOn(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ConfirmationToken setModifiedOn(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    public ConfirmationToken setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public ConfirmationToken setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
        return this;
    }

    public ConfirmationToken setAppUser(AppUser appUser) {
        this.appUser = appUser;
        return this;
    }
}
