package com.senpare.paymentservice.repository;

import com.senpare.paymentservice.dto.PaymentStatus;
import com.senpare.paymentservice.model.PaymentLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface PaymentLogRepository extends JpaRepository<PaymentLog, UUID> {

    Optional<PaymentLog> findByTransactionReference(String txRef);

    Page<PaymentLog> findAllByEmail(Pageable pageable, String email);

    @Query("SELECT SUM(p.amount) FROM PaymentLog  p WHERE p.status=?1")
    BigDecimal getnGeneratedRevenue(PaymentStatus status);
}
