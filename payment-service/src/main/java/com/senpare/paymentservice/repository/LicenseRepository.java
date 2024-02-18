package com.senpare.paymentservice.repository;

import com.senpare.paymentservice.dto.LicenseStatus;
import com.senpare.paymentservice.model.License;
import com.senpare.paymentservice.model.LicenseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LicenseRepository extends JpaRepository<License, UUID> {

    Optional<License> findByLicenseKey(UUID licenseKey);

    Page<License> findAllByEmail(String email, Pageable pageable);

    Page<License> findAllByEmailAndLicenseStatus(String email, LicenseStatus licenseStatus, Pageable pageable);


    boolean existsByLicenseType(LicenseType licenseType);
}
