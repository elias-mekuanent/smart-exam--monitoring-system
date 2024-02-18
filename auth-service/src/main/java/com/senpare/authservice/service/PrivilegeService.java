package com.senpare.authservice.service;

import java.util.List;
import java.util.UUID;

import com.senpare.authservice.exception.ResourceAlreadyExistsException;
import com.senpare.authservice.utilities.AuthServiceMessages;
import com.senpare.authservice.utilities.Util;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.senpare.authservice.exception.BadRequestException;
import com.senpare.authservice.exception.ResourceNotFoundException;
import com.senpare.authservice.model.Privilege;
import com.senpare.authservice.repository.PrivilegeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Service
@Slf4j
public class PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    public Privilege create(Privilege privilege) throws ResourceAlreadyExistsException {
        if (privilege == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("Privilege"));
        }

        String privilegeName = privilege.getPrivilegeName();

        if (isPrivilegeNameTaken(privilegeName)) {
            throw new ResourceAlreadyExistsException(AuthServiceMessages.alreadyExists("Privilege", "name", privilegeName));
        }

        if(privilege.getUuid() == null){
            privilege.setUuid(UUID.randomUUID());
        }
        return privilegeRepository.save(privilege);
    }

    public List<Privilege> getPrivileges() {
        return privilegeRepository.findAll();
    }
    public Page<Privilege> getPrivileges(int page, int size, String sortBy) {
        return Util.paginate(privilegeRepository, page, size,sortBy);
    }


    public Privilege getPrivilegeByName(String privilegeName) {
        if (privilegeName == null || privilegeName.isEmpty()) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNullOrEmpty("Privilege.name"));
        }
        return privilegeRepository
                .findByPrivilegeName(privilegeName)
                .orElseThrow(() -> new ResourceNotFoundException(AuthServiceMessages.canNotBeFound("Privilege", "name", privilegeName)));
    }

    public Privilege getPrivilege(UUID uuid) {
        if (uuid == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("Privilege.uuid"));
        }
        return privilegeRepository
                .findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(AuthServiceMessages.canNotBeFound("Privliege", "uuid", uuid.toString())));
    }

    public boolean isPrivilegeNameTaken(String privilegeName) {
        if (privilegeName == null || privilegeName.isEmpty()) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNullOrEmpty("privilegeName"));
        }
        return privilegeRepository
                .findByPrivilegeName(privilegeName)
                .isPresent();
    }

}