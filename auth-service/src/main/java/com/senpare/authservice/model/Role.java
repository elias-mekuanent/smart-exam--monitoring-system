package com.senpare.authservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "role")
@Getter
public class Role extends AuditableBean {

    @Id
    @Column(name = "uuid", nullable = false)
    private UUID uuid;
    @NotBlank(message = "Role name field required")
    @Column(name = "role_name", nullable = false)
    private String roleName;
    @Column(name = "role_description")
    private String roleDescription;
    @ManyToMany(targetEntity = Privilege.class,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "privilege_roles",
            joinColumns = @JoinColumn(name = "role_uuid", referencedColumnName = "uuid"),
            inverseJoinColumns =  @JoinColumn(name = "privilege_uuid", referencedColumnName = "uuid")
    )
    private Set<Privilege> privileges;

    public Role setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public Role setRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public Role setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
        return this;
    }

    public Role setPrivileges(Set<Privilege> privileges) {
        this.privileges = privileges;
        return this;
    }

    @Override
    public Role setCreatedOn(LocalDateTime createdOn) {
        return (Role) super.setCreatedOn(createdOn);
    }

    @Override
    public Role setCreatedBy(String createdBy) {
        return (Role) super.setCreatedBy(createdBy);
    }

    @Override
    public Role setModifiedOn(LocalDateTime modifiedOn) {
        return (Role) super.setModifiedOn(modifiedOn);
    }

    @Override
    public Role setModifiedBy(String modifiedBy) {
        return (Role) super.setModifiedBy(modifiedBy);
    }

}
