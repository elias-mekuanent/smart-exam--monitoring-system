package com.senpare.authservice.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
public class RoleDTO {

    private UUID uuid;
    private String roleName;
    private String roleDescription;
    private Set<String> privileges;
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;
    private String createdBy;
    private String modifiedBy;


    public RoleDTO setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public RoleDTO setRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public RoleDTO setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
        return this;
    }

    public RoleDTO setPrivileges(Set<String> privileges) {
        this.privileges = privileges;
        return this;
    }

    public RoleDTO setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public RoleDTO setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
        return this;
    }

    public RoleDTO setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public RoleDTO setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }
}
