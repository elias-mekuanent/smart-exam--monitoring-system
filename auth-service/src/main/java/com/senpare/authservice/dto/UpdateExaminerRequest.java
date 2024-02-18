package com.senpare.authservice.dto;

public class UpdateExaminerRequest extends UpdateAppUserRequest {

    private String phoneNumber;
    private String accountType;
    private String organization;
    private String roleInOrganization;

    @Override
    public UpdateExaminerRequest setFirstName(String firstName) {
        return (UpdateExaminerRequest) super.setFirstName(firstName);
    }

    @Override
    public UpdateExaminerRequest setLastName(String lastName) {
        return (UpdateExaminerRequest) super.setLastName(lastName);
    }

    @Override
    public UpdateExaminerRequest setEmail(String email) {
        return (UpdateExaminerRequest) super.setEmail(email);
    }


    public UpdateExaminerRequest setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public UpdateExaminerRequest setAccountType(String accountType) {
        this.accountType = accountType;
        return this;
    }

    public UpdateExaminerRequest setOrganization(String organization) {
        this.organization = organization;
        return this;
    }

    public UpdateExaminerRequest setRoleInOrganization(String roleInOrganization) {
        this.roleInOrganization = roleInOrganization;
        return this;
    }
    
}
