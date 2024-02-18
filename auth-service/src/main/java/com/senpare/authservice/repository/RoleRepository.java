package com.senpare.authservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.senpare.authservice.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByRoleName(String roleName);

    @Query(value = "SELECT COUNT(*) FROM appuser_roles ar WHERE ar.role_id=?1 LIMIT ?2", nativeQuery = true)
    int findByRole(UUID uuid, int limit);

}