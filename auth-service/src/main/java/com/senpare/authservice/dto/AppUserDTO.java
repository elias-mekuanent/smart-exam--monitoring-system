package com.senpare.authservice.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
public class AppUserDTO {

    private UUID uuid;
    private String firstName;
    private String lastName;
    private String email;
    private Set<String> roles;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Boolean enabled;

    public AppUserDTO setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public AppUserDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public AppUserDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public AppUserDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public AppUserDTO setRoles(Set<String> roles) {
        this.roles = roles;
        return this;
    }

    public AppUserDTO setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public AppUserDTO setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    public AppUserDTO setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }
}
