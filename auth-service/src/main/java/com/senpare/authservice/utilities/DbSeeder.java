package com.senpare.authservice.utilities;

import com.senpare.authservice.dto.AppUserRequest;
import com.senpare.authservice.dto.RoleRequest;
import com.senpare.authservice.exception.ResourceAlreadyExistsException;
import com.senpare.authservice.model.AppUser;
import com.senpare.authservice.model.Privilege;
import com.senpare.authservice.model.Role;
import com.senpare.authservice.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
@Profile({"dev", "prod"})
public class DbSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private static boolean alreadySetup = false;
    private final AppUserService appUserService;
    private final RoleService roleService;
    private final PrivilegeService privilegeService;
    private final RolePrivilegeService rolePrivilegeService;
    private final AppUserRoleService appUserRoleService;


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        if (!roleService.getRoles().isEmpty()) {
            alreadySetup = true;
            return;
        }

        //create default privilege
        Set<Privilege> privileges = new HashSet<>();
        privileges.add(new Privilege().setPrivilegeName("ROLE-CREATE"));
        privileges.add(new Privilege().setPrivilegeName("ROLE-LIST"));
        privileges.add(new Privilege().setPrivilegeName("ROLE-DETAIL"));
        privileges.add(new Privilege().setPrivilegeName("ROLE-EDIT"));
        privileges.add(new Privilege().setPrivilegeName("ROLE-DELETE"));
        privileges.add(new Privilege().setPrivilegeName("ROLE-ASSIGN_PRIVILEGE"));
        privileges.add(new Privilege().setPrivilegeName("PRIVILEGE-LIST"));
        privileges.add(new Privilege().setPrivilegeName("PRIVILEGE-DETAIL"));
        privileges.add(new Privilege().setPrivilegeName("APPUSER-CREATE"));
        privileges.add(new Privilege().setPrivilegeName("APPUSER-LIST"));
        privileges.add(new Privilege().setPrivilegeName("APPUSER-DETAIL"));
        privileges.add(new Privilege().setPrivilegeName("APPUSER-EDIT"));
        privileges.add(new Privilege().setPrivilegeName("APPUSER-DELETE"));
        privileges.add(new Privilege().setPrivilegeName("APPUSER-ASSIGN_ROLE"));
        privileges.add(new Privilege().setPrivilegeName("APPUSER-RESET-PASSWORD"));

        try {
            for (Privilege privilege : privileges) {
                privilegeService.create(privilege);
            }
        } catch (ResourceAlreadyExistsException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        List<String> privilegeUuids = privileges.stream()
                .map(privilege -> privilege.getUuid().toString())
                .collect(Collectors.toList());

        //create default roles
        Set<RoleRequest> roles = new HashSet<>();
        RoleRequest superAdminRole = new RoleRequest()
                .setRoleName("ROLE_SUPER_ADMIN")
                .setRoleDescription("Unlimited global access. Accounts with this can't be deleted.");
        RoleRequest adminRole = new RoleRequest()
                .setRoleName("ROLE_ADMIN")
                .setRoleDescription("Unlimited global access. The only difference with SUPER_ADMIN role is,  accounts with this can be deleted.");
        RoleRequest examinerRole = new RoleRequest()
                .setRoleName("ROLE_EXAMINER")
                .setRoleDescription("Role for examiners.");
        RoleRequest examineeRole = new RoleRequest()
                .setRoleName("ROLE_EXAMINEE")
                .setRoleDescription("Role for examinees.");
        RoleRequest userRole = new RoleRequest()
                .setRoleName("ROLE_USER")
                .setRoleDescription("Limited Access");


        roles.add(superAdminRole);
        roles.add(adminRole);
        roles.add(examinerRole);
        roles.add(examineeRole);
        roles.add(userRole);
        Map<String, UUID> roleNameUuidMapping = new HashMap<>();
        try {
            for (RoleRequest role : roles) {
                Role createdRole = roleService.create(role);
                roleNameUuidMapping.put(createdRole.getRoleName(), createdRole.getUuid());
            }
        } catch (ResourceAlreadyExistsException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        rolePrivilegeService.assignPrivilege(roleNameUuidMapping.get("ROLE_SUPER_ADMIN"), privilegeUuids);
        rolePrivilegeService.assignPrivilege(roleNameUuidMapping.get("ROLE_ADMIN"), privilegeUuids);

        List<String> roleUuids = roles.stream()
                .map(role -> roleService.getRoleByName(role.getRoleName())
                        .getUuid()
                        .toString())
                .collect(Collectors.toList());

        //create super admin user
        AppUserRequest adminAppUser = new AppUserRequest()
                .setFirstName("admin")
                .setLastName("admin")
                .setEmail("admin@admin.com")
                .setPassword("admin");
        try {
            AppUser createAppUser = appUserService.create(adminAppUser, true);
            appUserRoleService.assignRoleByRoleUuids(createAppUser.getUuid(), roleUuids);
        } catch (ResourceAlreadyExistsException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        //create super admin user
//        AppUserRequest user = new AppUserRequest()
//                .setFirstName("user")
//                .setLastName("user")
//                .setEmail("user@user.com")
//                .setPassword("user");
//        try {
//            appUserService.create(user, true);
//        } catch (ResourceAlreadyExistsException e) {
//            log.error(e.getMessage(), e);
//            throw new RuntimeException(e);
//        }

        log.info("Database successfully seeded");
        alreadySetup = true;
    }

}