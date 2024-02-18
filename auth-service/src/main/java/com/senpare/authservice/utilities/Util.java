package com.senpare.authservice.utilities;

import com.senpare.authservice.dto.AppUserDTO;
import com.senpare.authservice.dto.RoleDTO;
import com.senpare.authservice.model.AppUser;
import com.senpare.authservice.model.Privilege;
import com.senpare.authservice.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Util {

    public static <T, R extends JpaRepository> Page<T> paginate(R repository, int page, int size, String sortBy) {
        Pageable pageRequest = getPageable(page, size, sortBy);

        return repository.findAll(pageRequest == null ? Pageable.unpaged() : pageRequest);
    }

    public static Pageable getPageable(int page, int size, String sortBy) {
        int actualPage = page - 1;
        Pageable pageRequest;
        if(sortBy != null && !sortBy.isEmpty()) {
            if(sortBy.startsWith("-")) {
                sortBy = sortBy.substring(1);
                pageRequest =  PageRequest.of(actualPage, size, Sort.by(Sort.Direction.DESC, sortBy));
            } else {
                pageRequest =  PageRequest.of(actualPage, size, Sort.by(Sort.Direction.ASC, sortBy));
            }
        } else {
            pageRequest =  PageRequest.of(actualPage, size);
        }
        return pageRequest;
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotNullAndEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    public static List<AppUserDTO> toAppUserDTOS(List<AppUser> appUsers) {
        return appUsers.stream()
                .map(appUser -> toAppUserDTO(appUser))
                .collect(Collectors.toList());
    }

    public static AppUserDTO toAppUserDTO(AppUser appUser) {
        return new AppUserDTO()
                        .setUuid(appUser.getUuid())
                        .setFirstName(appUser.getFirstName())
                        .setLastName(appUser.getLastName())
                        .setEmail(appUser.getEmail())
                        .setRoles(appUser.getRoles() != null ? appUser.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()) : Collections.emptySet())
                        .setEnabled(appUser.getEnabled())
                        .setCreatedAt(appUser.getCreatedOn())
                        .setModifiedAt(appUser.getModifiedOn());
    }

    public static List<RoleDTO> toRoleDTOS(List<Role> roles) {
        return roles.stream()
                .map(role -> toRoleDTO(role))
                .collect(Collectors.toList());
    }

    public static RoleDTO toRoleDTO(Role role) {
        return new RoleDTO()
                    .setUuid(role.getUuid())
                    .setRoleName(role.getRoleName())
                    .setRoleDescription(role.getRoleDescription())
                    .setPrivileges(role.getPrivileges().stream().map(Privilege::getPrivilegeName).collect(Collectors.toSet()))
                    .setCreatedOn(role.getCreatedOn())
                    .setModifiedOn(role.getModifiedOn())
                    .setCreatedBy(role.getCreatedBy())
                    .setModifiedBy(role.getModifiedBy());
    }


}
