package com.senpare.examservice.client.response;


import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
public class AppUser {

    private UUID uuid;
    private String firstName;
    private String lastName;
    private String email;
    private Set<String> roles;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Boolean enabled;

    public AppUser setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public AppUser setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public AppUser setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public AppUser setEmail(String email) {
        this.email = email;
        return this;
    }

    public AppUser setRoles(Set<String> roles) {
        this.roles = roles;
        return this;
    }

    public AppUser setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public AppUser setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    public AppUser setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }
}
