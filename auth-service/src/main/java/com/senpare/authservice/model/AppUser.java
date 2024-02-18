package com.senpare.authservice.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.senpare.authservice.model.enumeration.ExaminerAccountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "app_user")
@SecondaryTables({
        @SecondaryTable(name = "examiner"),
        @SecondaryTable(name = "examinee")
})
@Getter
public class AppUser extends AuditableBean {

    @Id
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @NotBlank(message = "Firstname field required")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Lastname field required")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "phone_number", table = "examiner")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", table = "examiner")
    private ExaminerAccountType accountType;

    @Column(table = "examiner")
    private String organization;

    @Column(name = "role_in_organization", table = "examiner")
    private String roleInOrganization;

    @Column(name = "exam_uuid", nullable = false, table = "examinee")
    private UUID examUUid;

    @ManyToMany(targetEntity = Role.class,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "app_user_roles",
            joinColumns = @JoinColumn(name = "appuser_uuid", referencedColumnName = "uuid"),
            inverseJoinColumns =  @JoinColumn(name = "role_uuid", referencedColumnName = "uuid")
    )
    private Set<Role> roles;
    private Boolean enabled = false;

    @JsonIgnore
    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private ConfirmationToken confirmationToken;

    @JsonIgnore
    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private AppUserImage appUserImage;

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

    public AppUser setPassword(String password) {
        this.password = password;
        return this;
    }

    public AppUser setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public AppUser setAccountType(ExaminerAccountType accountType) {
        this.accountType = accountType;
        return this;
    }

    public AppUser setAccountType(String accountType) {
        this.accountType = ExaminerAccountType.lookupByCode(accountType);
        return this;
    }

    public AppUser setOrganization(String organization) {
        this.organization = organization;
        return this;
    }

    public AppUser setRoleInOrganization(String roleInOrganization) {
        this.roleInOrganization = roleInOrganization;
        return this;
    }

    public AppUser setExamUUid(UUID examUUid) {
        this.examUUid = examUUid;
        return this;
    }

    public AppUser setExamUUid(String examUUid) {
        this.examUUid = UUID.fromString(examUUid);
        return this;
    }

    public AppUser setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public AppUser setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public AppUser setCreatedOn(LocalDateTime createdOn) {
        return (AppUser) super.setCreatedOn(createdOn);
    }

    @Override
    public AppUser setCreatedBy(String createdBy) {
        return (AppUser) super.setCreatedBy(createdBy);
    }

    @Override
    public AppUser setModifiedOn(LocalDateTime modifiedOn) {
        return (AppUser) super.setModifiedOn(modifiedOn);
    }

    @Override
    public AppUser setModifiedBy(String modifiedBy) {
        return (AppUser) super.setModifiedBy(modifiedBy);
    }

}
