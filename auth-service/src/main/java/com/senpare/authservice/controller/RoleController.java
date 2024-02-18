package com.senpare.authservice.controller;

import com.senpare.authservice.dto.RoleDTO;
import com.senpare.authservice.model.Role;
import com.senpare.authservice.service.RolePrivilegeService;
import com.senpare.authservice.service.RoleService;
import com.senpare.authservice.utilities.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth-service/roles")
@Tag(name= "Role endpoint", description = "CRUD operation on system role")
public class RoleController {

    private final RoleService roleService;
    private final RolePrivilegeService rolePrivilegeService;

//    @PostMapping
//    @Operation(summary = "Create roleRequest for users")
//    @PreAuthorize("hasAuthority('ROLE-CREATE')")
//    public ResponseEntity<RoleDTO> createRole(@RequestBody final RoleRequest roleRequest, HttpServletRequest request) {
//        Role createdRole = roleService.create(roleRequest);
//        URI location = ServletUriComponentsBuilder.fromRequestUri(request)
//                .path("/" + createdRole.getUuid())
//                .build()
//                .toUri();
//
//        HttpHeaders responseHeaders = new HttpHeaders();
//
//        responseHeaders.setAllow(Set.of(HttpMethod.GET, HttpMethod.POST));
//        responseHeaders.setLocation(location);
//        return new ResponseEntity<>(Util.toRoleDTO(createdRole), responseHeaders, HttpStatus.CREATED);
//    }

    @GetMapping
    @Operation(summary = "Get all roles")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<List<RoleDTO>> getRoles(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                  @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                                  @RequestParam(value = "sort", required = false, defaultValue = "roleName") String sort) {
        Page<Role> roles = roleService.getRoles(page, size, sort);
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.add("Pagination-Count", String.valueOf(roles.getTotalElements()));
        responseHeaders.add("Pagination-Page", String.valueOf(roles.getTotalPages()));


        responseHeaders.setAllow(Set.of(HttpMethod.GET, HttpMethod.POST));
        return new ResponseEntity<>(Util.toRoleDTOS(roles.getContent()), responseHeaders, HttpStatus.OK);
    }

    @GetMapping("{uuid}")
    @Operation(summary = "Get role by uuid")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<RoleDTO> getRole(@PathVariable("uuid") String uuid) {
        Role role = roleService.getRole(UUID.fromString(uuid));
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.setAllow(Set.of(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE));
        return new ResponseEntity<>(Util.toRoleDTO(role), responseHeaders, HttpStatus.OK);
    }

//    @PutMapping("{uuid}")
//    @Operation(summary = "Update role by uuid")
//    @PreAuthorize("hasAuthority('ROLE-UPDATE')")
//    public ResponseEntity<RoleDTO> updateRole(@PathVariable("uuid") String uuid, @RequestBody RoleRequest request) {
//        Role updatedRole = roleService.update(UUID.fromString(uuid), request);
//        HttpHeaders responseHeaders = new HttpHeaders();
//
//        responseHeaders.setAllow(Set.of(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE));
//        return new ResponseEntity<>(Util.toRoleDTO(updatedRole), responseHeaders, HttpStatus.OK);
//    }
//
//    @DeleteMapping("{uuid}")
//    @Operation(summary = "Delete role by uuid")
//    @PreAuthorize("hasAuthority('ROLE-DELETE')")
//    public ResponseEntity<Void> deleteRole(@PathVariable("uuid") UUID uuid) {
//        roleService.delete(uuid);
//        HttpHeaders responseHeaders = new HttpHeaders();
//
//        responseHeaders.setAllow(Set.of(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE));
//        return new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT);
//    }
//
//    @PutMapping("{uuid}/assign-privilege")
//    @Operation(summary = "Assign privileges to the specified role")
//    @PreAuthorize("hasAuthority('ROLE-ASSIGN_PRIVILEGE')")
//    public ResponseEntity<Void> assignPrivileges(@PathVariable("uuid") UUID uuid, @RequestBody List<String> privilegeUuids) {
//        rolePrivilegeService.assignPrivilege(uuid, privilegeUuids);
//        HttpHeaders responseHeaders = new HttpHeaders();
//
//        responseHeaders.setAllow(Set.of(HttpMethod.PUT));
//        return new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT);
//    }

}
