package com.senpare.authservice.service;

import com.senpare.authservice.exception.BadRequestException;
import com.senpare.authservice.exception.ResourceNotFoundException;
import com.senpare.authservice.model.AppUser;
import com.senpare.authservice.model.Role;
import com.senpare.authservice.repository.AppUserRepository;
import com.senpare.authservice.utilities.AuthServiceMessages;
import com.senpare.authservice.utilities.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AppUserRoleService {

    private final AppUserRepository appUserRepository;
    private final RoleService roleService;

    public AppUser assignRoleByRoleUuids(UUID appUserUuid, List<String> roleUuids) {
        if (appUserUuid == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("AppUser.uuid"));
        }

        if (Util.isNullOrEmpty(roleUuids)) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNullOrEmpty("Role.uuid list"));
        }

        AppUser appUser = appUserRepository.findById(appUserUuid)
                .orElseThrow(() -> new ResourceNotFoundException(AuthServiceMessages.canNotBeFound("AppUser", "uuid", appUserUuid.toString())));
        Set<Role> roleSet = roleUuids.stream()
                .map(roleName -> roleService.getRole(UUID.fromString(roleName)))
                .collect(Collectors.toSet());
        appUser.setRoles(roleSet);

        return appUserRepository.save(appUser);
    }

    public AppUser assignRoleByRoleNames(UUID appUserUuid, List<String> roleNames) {
        if (appUserUuid == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("AppUser.uuid"));
        }

        if (Util.isNullOrEmpty(roleNames)) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNullOrEmpty("Role.name list"));
        }

        AppUser appUser = appUserRepository.findById(appUserUuid)
                .orElseThrow(() -> new ResourceNotFoundException(AuthServiceMessages.canNotBeFound("AppUser", "uuid", appUserUuid.toString())));
        Set<Role> roleSet = roleNames.stream()
                .map(roleService::getRoleByName)
                .collect(Collectors.toSet());
        appUser.setRoles(roleSet);

        return appUserRepository.save(appUser);
    }

}
