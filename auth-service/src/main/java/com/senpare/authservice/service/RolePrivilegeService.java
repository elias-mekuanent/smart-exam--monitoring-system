package com.senpare.authservice.service;

import com.senpare.authservice.exception.BadRequestException;
import com.senpare.authservice.exception.ResourceNotFoundException;
import com.senpare.authservice.model.Privilege;
import com.senpare.authservice.model.Role;
import com.senpare.authservice.repository.RoleRepository;
import com.senpare.authservice.utilities.AuthServiceMessages;
import com.senpare.authservice.utilities.Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RolePrivilegeService {

    private final RoleRepository roleRepository;
    private final PrivilegeService privilegeService;

    @Transactional
    public Role assignPrivilege(UUID roleUuid, List<String> privilegesUuids) {
        if (roleUuid == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("Role.uuid"));
        }

        if (Util.isNullOrEmpty(privilegesUuids)) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNullOrEmpty("Privilege.uuid list"));
        }

        Role role = roleRepository.findById(roleUuid)
                .orElseThrow(() -> new ResourceNotFoundException(AuthServiceMessages.canNotBeFound("Role", "uuid", roleUuid.toString())));
        Set<Privilege> privilegeSet = privilegesUuids.stream()
                .map(privilegesUuid -> privilegeService.getPrivilege(UUID.fromString(privilegesUuid)))
                .collect(Collectors.toSet());
        role.setPrivileges(privilegeSet);

        return roleRepository.save(role);
    }
}
