package com.senpare.authservice.dto;

import lombok.Getter;

@Getter
public class ExaminerRegistrationRequest extends AppUserRequest {

    private String phoneNumber;
    private String accountType;
    private String organization;
    private String roleInOrganization;

    @Override
    public ExaminerRegistrationRequest setFirstName(String firstName) {
        return (ExaminerRegistrationRequest) super.setFirstName(firstName);
    }

    @Override
    public ExaminerRegistrationRequest setLastName(String lastName) {
        return (ExaminerRegistrationRequest) super.setLastName(lastName);
    }

    @Override
    public ExaminerRegistrationRequest setEmail(String email) {
        return (ExaminerRegistrationRequest) super.setEmail(email);
    }

    @Override
    public ExaminerRegistrationRequest setPassword(String password) {
        return (ExaminerRegistrationRequest) super.setPassword(password);
    }

    public ExaminerRegistrationRequest setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public ExaminerRegistrationRequest setAccountType(String accountType) {
        this.accountType = accountType;
        return this;
    }

    public ExaminerRegistrationRequest setOrganization(String organization) {
        this.organization = organization;
        return this;
    }

    public ExaminerRegistrationRequest setRoleInOrganization(String roleInOrganization) {
        this.roleInOrganization = roleInOrganization;
        return this;
    }
}
