package com.senpare.paymentservice.repository;

import com.senpare.paymentservice.model.License;
import com.senpare.paymentservice.model.LicenseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LicenseTypeRepository extends JpaRepository<LicenseType, UUID> {

    Optional<LicenseType> findByTypeName(String typeName);

    Optional<LicenseType> findByAmount(BigDecimal amount);

}
