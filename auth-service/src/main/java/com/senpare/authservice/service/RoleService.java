package com.senpare.authservice.service;

import com.senpare.authservice.dto.RoleRequest;
import com.senpare.authservice.exception.BadRequestException;
import com.senpare.authservice.exception.ResourceAlreadyExistsException;
import com.senpare.authservice.exception.ResourceNotFoundException;
import com.senpare.authservice.model.Privilege;
import com.senpare.authservice.model.Role;
import com.senpare.authservice.repository.RoleRepository;
import com.senpare.authservice.utilities.AuthServiceMessages;
import com.senpare.authservice.utilities.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;
    private final PrivilegeService privilegeService;

    public Role create(RoleRequest request) {
        if (request == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("Role"));
        }

        String roleName = request.getRoleName().toUpperCase();
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }

        if (isRoleNameTaken(roleName)) {
            throw new ResourceAlreadyExistsException(AuthServiceMessages.alreadyExists("Role", "name", roleName));
        }
        request.setRoleName(roleName);

        Role role = createRoleFromRequest(request);
        return roleRepository.save(role);
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public Page<Role> getRoles(int page, int size, String sortBy) {
        return Util.paginate(roleRepository, page, size, sortBy);
    }

    public Role getRoleByName(String roleName) {

        return roleRepository
                .findByRoleName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException(AuthServiceMessages.canNotBeFound("Role", "roleName", roleName)));
    }

    public Role getRole(UUID uuid) {
        if (uuid == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("Role.uuid"));
        }
        return roleRepository
                .findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(AuthServiceMessages.canNotBeFound("Role","uuid", uuid.toString())));
    }

    public Role update(UUID uuid, RoleRequest roleRequest) {
        if (uuid == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("Role.uuid"));
        }

        if (roleRequest == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("Role"));
        }
        Role role = getRole(uuid);
        boolean isUpdated = false;
        String roleName = roleRequest.getRoleName();
        String roleDescription = roleRequest.getRoleDescription();

        if(roleName != null && Objects.equals(roleName, role.getRoleName())) {
            if (isRoleNameTaken(roleName)) {
                throw new ResourceAlreadyExistsException(AuthServiceMessages.alreadyExists("Role", "name", roleName));
            }
            role.setRoleName(roleName);
            isUpdated = true;
        }

        if(roleDescription != null && Objects.equals(roleDescription, role.getRoleDescription())) {
            role.setRoleName(roleDescription);
            isUpdated = true;
        }

        if(isUpdated) {
            role = roleRepository.save(role);
        }
        return role;
    }

    public Role delete(UUID uuid) {
        Role role = getRole(uuid);
        roleRepository.deleteById(uuid);
        return role;
    }

    public boolean isRoleNameTaken(String roleName) {
        if (Util.isNullOrEmpty(roleName)) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNullOrEmpty("Role.name"));
        }
        return roleRepository
                .findByRoleName(roleName)
                .isPresent();
    }

    private Role createRoleFromRequest(RoleRequest roleRequest) {
        return new Role()
                .setUuid(UUID.randomUUID())
                .setRoleName(roleRequest.getRoleName())
                .setRoleDescription(roleRequest.getRoleDescription());
    }
}