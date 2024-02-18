package com.senpare.authservice.repository;

import com.senpare.authservice.model.AppUser;
import com.senpare.authservice.model.AppUserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppUserImageRepository extends JpaRepository<AppUserImage, UUID> {

    Optional<AppUserImage> findByAppUser(AppUser appUser);
}
