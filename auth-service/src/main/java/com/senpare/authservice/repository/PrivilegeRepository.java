package com.senpare.authservice.repository;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.senpare.authservice.model.Privilege;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, UUID> {

    Optional<Privilege> findByPrivilegeName(String privilegeName);
}
