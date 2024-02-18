package com.senpare.authservice.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "app_user_image")
@Getter
public class AppUserImage extends AuditableBean{

    @Id
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @OneToOne
    @JoinColumn(name = "app_user_uuid", referencedColumnName = "uuid", unique = true, nullable = false)
    private AppUser appUser;

    private byte[] image;

    public AppUserImage setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public AppUserImage setAppUser(AppUser appUser) {
        this.appUser = appUser;
        return this;
    }

    public AppUserImage setImage(byte[] image) {
        this.image = image;
        return this;
    }

    @Override
    public AppUserImage setCreatedOn(LocalDateTime createdOn) {
        return (AppUserImage) super.setCreatedOn(createdOn);
    }

    @Override
    public AppUserImage setCreatedBy(String createdBy) {
        return (AppUserImage) super.setCreatedBy(createdBy);
    }

    @Override
    public AppUserImage setModifiedOn(LocalDateTime modifiedOn) {
        return (AppUserImage) super.setModifiedOn(modifiedOn);
    }

    @Override
    public AppUserImage setModifiedBy(String modifiedBy) {
        return (AppUserImage) super.setModifiedBy(modifiedBy);
    }
}
