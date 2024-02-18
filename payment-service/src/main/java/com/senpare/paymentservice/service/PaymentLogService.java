package com.senpare.paymentservice.service;

import com.senpare.paymentservice.dto.LicenseRequest;
import com.senpare.paymentservice.dto.PaymentLogRequest;
import com.senpare.paymentservice.dto.PaymentStatus;
import com.senpare.paymentservice.exception.BadRequestException;
import com.senpare.paymentservice.exception.ResourceNotFoundException;
import com.senpare.paymentservice.model.License;
import com.senpare.paymentservice.model.PaymentLog;
import com.senpare.paymentservice.repository.PaymentLogRepository;
import com.senpare.paymentservice.utilities.PaymentServiceMessages;
import com.senpare.paymentservice.utilities.Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentLogService {

    private final PaymentLogRepository paymentLogRepository;
    private final LicenseService licenseService;

    public PaymentLog create(PaymentLogRequest request) {
        if(request == null) {
            throw new BadRequestException(PaymentServiceMessages.canNotBeNull("PaymentLogRequest"));
        }

        PaymentLog paymentLog = createPaymentLogFromRequest(request);

        return paymentLogRepository.save(paymentLog);
    }

    public PaymentLog get(UUID uuid) {
        if(uuid == null) {
            throw new BadRequestException(PaymentServiceMessages.canNotBeNull("PaymentLog.uuid"));
        }

        return paymentLogRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(PaymentServiceMessages.canNotBeFound("PaymentLog", "uuid", uuid.toString())));
    }

    public PaymentLog getByTransactionRef(String txRef) {
        if(Util.isNullOrEmpty(txRef)) {
            throw new BadRequestException(PaymentServiceMessages.canNotBeNull("PaymentLog.txRef"));
        }

        return paymentLogRepository.findByTransactionReference(txRef)
                .orElseThrow(() -> new ResourceNotFoundException(PaymentServiceMessages.canNotBeFound("PaymentLog","txRef", txRef)));
    }

    public Page<PaymentLog> getAll(int page, int size, String sortBy) {
        return Util.paginate(paymentLogRepository, page, size, sortBy);
    }

    public Page<PaymentLog> getAllByEmail(int page, int size, String sortBy, String email) {
        if(Util.isNotNullAndEmpty(email)) {
            throw new BadRequestException(PaymentServiceMessages.canNotBeNull("PaymentLog.email"));
        }

        Pageable pageRequest = Util.getPageable(page, size, sortBy);
        return paymentLogRepository.findAllByEmail(pageRequest, email);
    }

    @Transactional
    public PaymentLog verifyPayment(String txRef) {
        if(Util.isNullOrEmpty(txRef)) {
            throw new BadRequestException(PaymentServiceMessages.canNotBeNull("PaymentLog.txRef"));
        }

        PaymentLog paymentLog = paymentLogRepository.findByTransactionReference(txRef)
                .orElseThrow(() -> new ResourceNotFoundException(PaymentServiceMessages.canNotBeFound("PaymentLog","txRef", txRef)));

        paymentLog.setStatus(PaymentStatus.SUCCESSFUL);

        log.info("Creating a licence after payment verification for transaction reference: {}", txRef);
        LicenseRequest request = new LicenseRequest()
                .setEmail(paymentLog.getEmail())
                .setAmount(paymentLog.getAmount());
        License license = licenseService.create(request);
        paymentLog.setLicense(license);

        return paymentLogRepository.save(paymentLog);
    }

    @Transactional
    public PaymentLog cancelPayment(String txRef) {
        if(Util.isNullOrEmpty(txRef)) {
            throw new BadRequestException(PaymentServiceMessages.canNotBeNull("PaymentLog.txRef"));
        }

        PaymentLog paymentLog = paymentLogRepository.findByTransactionReference(txRef)
                .orElseThrow(() -> new ResourceNotFoundException(PaymentServiceMessages.canNotBeFound("PaymentLog","txRef", txRef)));


        paymentLog.setStatus(PaymentStatus.FAILED);

        return paymentLogRepository.save(paymentLog);
    }

    public boolean isPaymentVerified(String txRef) {
        if(Util.isNullOrEmpty(txRef)) {
            throw new BadRequestException(PaymentServiceMessages.canNotBeNull("PaymentLog.txRef"));
        }

        PaymentLog paymentLog = paymentLogRepository.findByTransactionReference(txRef)
                .orElseThrow(() -> new ResourceNotFoundException(PaymentServiceMessages.canNotBeFound("PaymentLog","txRef", txRef)));

        return paymentLog.getStatus() == PaymentStatus.SUCCESSFUL;
    }


    private PaymentLog createPaymentLogFromRequest(PaymentLogRequest request) {
        return new PaymentLog()
                .setUuid(UUID.randomUUID())
                .setEmail(request.getEmail())
                .setPaymentProvider(request.getPaymentProvider())
                .setTransactionReference(request.getTransactionReference())
                .setAmount(request.getAmount())
                .setStatus(PaymentStatus.INITIALIZED)
                ;

    }

    public BigDecimal getGeneratedRevenue(PaymentStatus status) {
        if(status == null) {
            throw new BadRequestException(PaymentServiceMessages.canNotBeNull("PaymentLog.PaymentStatus"));
        }
        return paymentLogRepository.getnGeneratedRevenue(status);
    }
}
