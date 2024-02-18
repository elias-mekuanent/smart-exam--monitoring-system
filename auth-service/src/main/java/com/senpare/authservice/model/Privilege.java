package com.senpare.authservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "privilege")
@Getter
public class Privilege extends AuditableBean {

    @Id
    @Column(name = "uuid", nullable = false)
    private UUID uuid;
    @NotBlank(message = "Privilege name field required")
    @Column(nullable = false, updatable = false)
    private String privilegeName;
    private String privilegeDescription;

    public Privilege setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public Privilege setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
        return this;
    }

    public Privilege setPrivilegeDescription(String privilegeDescription) {
        this.privilegeDescription = privilegeDescription;
        return this;
    }

    @Override
    public Privilege setCreatedOn(LocalDateTime createdOn) {
        return (Privilege) super.setCreatedOn(createdOn);
    }

    @Override
    public Privilege setCreatedBy(String createdBy) {
        return (Privilege) super.setCreatedBy(createdBy);
    }

    @Override
    public Privilege setModifiedOn(LocalDateTime modifiedOn) {
        return (Privilege) super.setModifiedOn(modifiedOn);
    }

    @Override
    public Privilege setModifiedBy(String modifiedBy) {
        return (Privilege) super.setModifiedBy(modifiedBy);
    }

}
