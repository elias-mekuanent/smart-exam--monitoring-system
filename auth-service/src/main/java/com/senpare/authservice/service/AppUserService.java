package com.senpare.authservice.service;

import com.senpare.authservice.dto.AppUserRequest;
import com.senpare.authservice.dto.UpdateAppUserRequest;
import com.senpare.authservice.exception.BadRequestException;
import com.senpare.authservice.exception.ResourceAlreadyExistsException;
import com.senpare.authservice.exception.ResourceNotFoundException;
import com.senpare.authservice.model.AppUser;
import com.senpare.authservice.repository.AppUserRepository;
import com.senpare.authservice.repository.RoleRepository;
import com.senpare.authservice.security.AppUserDetails;
import com.senpare.authservice.utilities.AuthServiceMessages;
import com.senpare.authservice.utilities.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    public AppUser create(AppUserRequest request, boolean enableByDefault) {
        if (request == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("RegistrationRequest"));
        }

        if (isEmailTaken(request.getEmail())) {
            throw new ResourceAlreadyExistsException(AuthServiceMessages.alreadyExists("AppUser", "email", request.getEmail()));
        }

        AppUser appUser = createAppUserFromRequest(request);

        if (enableByDefault) {
            appUser.setEnabled(true);
        }

        // if no role found, assign ROLE_USER by default
        if (Util.isNullOrEmpty(appUser.getRoles())) {
            appUser.setRoles(Set.of(roleService.getRoleByName("ROLE_USER")));
        }

        appUser.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        appUser = appUserRepository.saveAndFlush(appUser);
        return appUser;
    }

    public AppUser getAppUser(UUID uuid) {
        if (uuid == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("AppUser.uuid"));
        }
        return appUserRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(AuthServiceMessages.canNotBeFound("AppUser", "uuid", uuid.toString())));
    }

    public AppUser getAppUserByEmail(String email) {
        if (Util.isNullOrEmpty(email)) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNullOrEmpty("Email"));
        }
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(AuthServiceMessages.canNotBeFound("User", "email", email)));
    }

    public int countExamineeInExam(String examUuid) {
        if (Util.isNullOrEmpty(examUuid)) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNullOrEmpty("Exam.examUuid"));
        }
        return appUserRepository.countAppUserByExamUUid(UUID.fromString(examUuid));
    }

    public Page<AppUser> getAppUsers(int page, int size, String sortBy) {
        Page<AppUser> appUserPage = Util.paginate(appUserRepository, page, size, sortBy);
        return appUserPage;
    }

    public Page<AppUser> getAppUsersByExamUuid(int page, int size, String sortBy, String examineUuid) {
        Pageable pageable = Util.getPageable(page, size, sortBy);
        Page<AppUser> appUserPage = appUserRepository.getAllByExamUUid(UUID.fromString(examineUuid), pageable);
        return appUserPage;
    }

    public AppUser update(UUID uuid, UpdateAppUserRequest request) {
        if (uuid == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("AppUser.uuid"));
        }

        if (request == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("UpdateAppUserRequest"));
        }

        AppUser existingAppUser = getAppUser(uuid);
        return update(existingAppUser, request);
    }

    public AppUser updateByEmail(String email, UpdateAppUserRequest request) {
        if (email == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("AppUser.email"));
        }

        if (request == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("UpdateAppUserRequest"));
        }

        AppUser existingAppUser = getAppUserByEmail(email);
        return update(existingAppUser, request);
    }

    private AppUser update(AppUser existingAppUser, UpdateAppUserRequest request) {
        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        String email = request.getEmail();
        boolean isUpdated = false;

        if (firstName != null && !Objects.equals(firstName, existingAppUser.getFirstName())) {
            existingAppUser.setFirstName(firstName);
            isUpdated = true;
        }

        if (lastName != null && !Objects.equals(lastName, existingAppUser.getLastName())) {
            existingAppUser.setLastName(lastName);
            isUpdated = true;
        }

        if (email != null && !Objects.equals(email, existingAppUser.getEmail())) {
            if (isEmailTaken(email)) {
                throw new ResourceAlreadyExistsException(AuthServiceMessages.alreadyExists("AppUser", "email", email));
            }
            existingAppUser.setEmail(email);
            isUpdated = true;
        }

        if (isUpdated) {
            appUserRepository.save(existingAppUser);
        }

        return existingAppUser;
    }

    public AppUser delete(UUID uuid) {
        AppUser appUser = getAppUser(uuid);

        appUserRepository.deleteById(uuid);
        return appUser;
    }

    public AppUser changePassword(UUID uuid, String oldPassword, String newPassword) {
        if (uuid == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("AppUser.uuid"));
        }

        AppUser appUser = getAppUser(uuid);
        return changePassword(appUser, oldPassword, newPassword);
    }

    public AppUser changePassword(String email, String oldPassword, String newPassword) {
        if (email == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("AppUser.email"));
        }

        AppUser appUser = getAppUserByEmail(email);
        return changePassword(appUser, oldPassword, newPassword);
    }

    private AppUser changePassword(AppUser appUser, String oldPassword, String newPassword) {
        if (Util.isNullOrEmpty(oldPassword)) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("Old password"));
        }

        if (Util.isNullOrEmpty(newPassword)) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("New password"));
        }

        if (!bCryptPasswordEncoder.matches(oldPassword, appUser.getPassword())) {
            throw new BadRequestException(AuthServiceMessages.isIncorrect("Old Password"));
        }
        appUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
        appUser = appUserRepository.save(appUser);
        return appUser;
    }

    public boolean isEmailTaken(String email) {
        if (Util.isNullOrEmpty(email)) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNullOrEmpty("AppUser.email"));
        }
        return appUserRepository
                .findByEmail(email)
                .isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AppUserDetails(getAppUserByEmail(username));
    }

    public AppUser enableUser(final AppUser appUser) {
        getAppUser(appUser.getUuid()); // this will throw exception if appUser doesn't exist
        appUser.setEnabled(true);
        return appUserRepository.save(appUser);
    }

    private AppUser createAppUserFromRequest(AppUserRequest request) {
        return new AppUser().setUuid(UUID.randomUUID())
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName())
                .setEmail(request.getEmail())
                .setPassword(request.getPassword());
    }

    private AppUser toAppUser(AppUser appUser, AppUserRequest request) {
        if (request.getFirstName() != null) {
            appUser.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            appUser.setLastName(request.getLastName());
        }

        if (request.getEmail() != null) {
            appUser.setEmail(request.getEmail());
        }

        if (request.getPassword() != null) {
            appUser.setPassword(request.getPassword());
        }

        return appUser;
    }
    public int getTotalNumberOfExaminers() {
        int totalNumberOfExaminers = appUserRepository.countByRoles(roleService.getRoleByName
                ("ROLE_EXAMINER"));
        return totalNumberOfExaminers;
    }

}
