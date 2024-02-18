package com.senpare.authservice.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.senpare.authservice.model.AppUser;
import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.senpare.authservice.model.ConfirmationToken;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, UUID> {

    Optional<ConfirmationToken> findByToken(String token);

    Optional<ConfirmationToken> findByAppUser(AppUser appUser);

    @Modifying
    @Query("DELETE FROM ConfirmationToken c WHERE c = ?1")
    int deleteToken(ConfirmationToken confirmationToken);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c SET c.confirmedAt=?2 WHERE c.token=?1")
    int updateConfirmedAt(String token, LocalDateTime confirmedAt);


}
