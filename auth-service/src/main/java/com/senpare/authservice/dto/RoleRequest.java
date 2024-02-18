package com.senpare.authservice.dto;

import lombok.Getter;

import java.util.Set;

@Getter
public class RoleRequest {

    private String roleName;
    private String roleDescription;

    public RoleRequest setRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public RoleRequest setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
        return this;
    }
}
