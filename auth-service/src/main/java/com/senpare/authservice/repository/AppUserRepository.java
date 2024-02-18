package com.senpare.authservice.repository;

import java.util.Optional;
import java.util.UUID;

import com.senpare.authservice.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.senpare.authservice.model.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

    Optional<AppUser> findByEmail(String email);

    @Modifying
    @Query("UPDATE AppUser a SET a.enabled = TRUE WHERE a.email = ?1")
    void enableAppUser(String email, Page page);

    int countAppUserByExamUUid(UUID uuid);

    Page<AppUser> getAllByExamUUid(UUID examUuid, Pageable pageable);

    int countByRoles(Role role);

}
