package com.senpare.paymentservice.service;

import com.senpare.paymentservice.dto.LicenseTypeRequest;
import com.senpare.paymentservice.exception.BadRequestException;
import com.senpare.paymentservice.exception.ResourceNotFoundException;
import com.senpare.paymentservice.model.LicenseType;
import com.senpare.paymentservice.repository.LicenseRepository;
import com.senpare.paymentservice.repository.LicenseTypeRepository;
import com.senpare.paymentservice.utilities.PaymentServiceMessages;
import com.senpare.paymentservice.utilities.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LicenseTypeService {

    private final LicenseTypeRepository licenseTypeRepository;
    private final LicenseRepository licenseRepository;

    public LicenseType create(LicenseTypeRequest request) {
        if(request == null) {
            throw new BadRequestException(PaymentServiceMessages.canNotBeNull("LicenseTypeRequest"));
        }

        validateUniqueTypeName(request.getTypeName());
        validateUniqueAmount(request.getAmount());

        LicenseType licenseType = createLicenseFromRequest(request);

        return licenseTypeRepository.save(licenseType);
    }

    public LicenseType get(UUID uuid) {
        if(uuid == null) {
            throw new BadRequestException(PaymentServiceMessages.canNotBeNull("LicenseType.uuid"));
        }

        return licenseTypeRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(PaymentServiceMessages.canNotBeFound("LicenseType", "uuid", uuid.toString())));
    }

    public LicenseType getByTypeName(String typeName) {
        if(Util.isNullOrEmpty(typeName)) {
            throw new BadRequestException(PaymentServiceMessages.canNotBeNull("LicenseType.typeName"));
        }

        return licenseTypeRepository.findByTypeName(typeName.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException(PaymentServiceMessages.canNotBeFound("LicenseType", "typeName", typeName)));
    }

    public LicenseType getByAmount(BigDecimal amount) {
        if(amount == null) {
            throw new BadRequestException(PaymentServiceMessages.canNotBeNull("LicenseType.amount"));
        }

        return licenseTypeRepository.findByAmount(amount)
                .orElseThrow(() -> new ResourceNotFoundException(PaymentServiceMessages.canNotBeFound("LicenseType", "amount", amount.toPlainString())));
    }

    public Page<LicenseType> getAll(int page, int size, String sortBy) {
        return Util.paginate(licenseTypeRepository, page, size, sortBy);
    }

    public LicenseType update(UUID uuid, LicenseTypeRequest request) {
        if(uuid == null) {
            throw new BadRequestException(PaymentServiceMessages.canNotBeNull("LicenseType.uuid"));
        }
        if(request == null) {
            throw new BadRequestException(PaymentServiceMessages.canNotBeNull("LicenseTypeRequest"));
        }

        LicenseType existingLicenseType = licenseTypeRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(PaymentServiceMessages.canNotBeFound("LicenseType", "uuid", uuid.toString())));

        if(!request.getTypeName().isEmpty() && !existingLicenseType.getTypeName().equals(request.getTypeName())) {
            validateUniqueTypeName(request.getTypeName());
        }

        if(request.getAmount() != null && !existingLicenseType.getAmount().equals(request.getAmount())) {
            validateUniqueAmount(request.getAmount());
        }

        if (Util.isNullOrEmpty(request.getTypeName() )) {
            request.setTypeName(existingLicenseType.getTypeName());
        }
        
        if (request.getAmount() == null) {
            request.setAmount(existingLicenseType.getAmount());
        }

        if (request.getAllowedExamineeCount() == null) {
            request.setAllowedExamineeCount(existingLicenseType.getAllowedExamineeCount());
        }

        if (request.getCameraSecurity() == null) {
            request.setCameraSecurity(existingLicenseType.getCameraSecurity());
        }

        if (request.getAudioRecordSecurity() == null) {
            request.setAudioRecordSecurity(existingLicenseType.getAudioRecordSecurity());
        }

        if (request.getScreenshotSecurity() == null) {
            request.setScreenshotSecurity(existingLicenseType.getScreenshotSecurity());
        }

        if (request.getMouseTrackSecurity() == null) {
            request.setMouseTrackSecurity(existingLicenseType.getMouseTrackSecurity());
        }

        toLicenseType(existingLicenseType, request);

        return licenseTypeRepository.save(existingLicenseType);
    }

    public LicenseType delete(UUID uuid) {
        LicenseType licenseType = licenseTypeRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(PaymentServiceMessages.canNotBeFound("LicenseType", "uuid", uuid.toString())));

        if(uuid == null) {
            throw new BadRequestException(PaymentServiceMessages.canNotBeNull("LicenseType.uuid"));
        }

        if (licenseRepository.existsByLicenseType(licenseType)) {
            throw new BadRequestException(PaymentServiceMessages.canNotDeleteResourceWithAssociatedResource("LicenseType" ,"licenses"));
        }

        licenseTypeRepository.delete(licenseType);
        return licenseType;
    }

    private LicenseType createLicenseFromRequest(LicenseTypeRequest request) {
        return new LicenseType()
                .setUuid(UUID.randomUUID())
                .setTypeName(request.getTypeName().toUpperCase())
                .setAmount(request.getAmount())
                .setAllowedExamineeCount(request.getAllowedExamineeCount())
                .setCameraSecurity(request.getCameraSecurity())
                .setAudioRecordSecurity(request.getAudioRecordSecurity())
                .setScreenshotSecurity(request.getScreenshotSecurity())
                .setMouseTrackSecurity(request.getMouseTrackSecurity())
                ;
    }

    private LicenseType toLicenseType(LicenseType licenseType, LicenseTypeRequest request) {
        if(Util.isNotNullAndEmpty(request.getTypeName())) {
            licenseType.setTypeName(request.getTypeName());
        }

        if(request.getAmount() != null) {
            licenseType.setAmount(request.getAmount());
        }

        if(request.getAllowedExamineeCount() != null) {
            licenseType.setAllowedExamineeCount(request.getAllowedExamineeCount());
        }

        if(request.getCameraSecurity() != null) {
            licenseType.setCameraSecurity(request.getCameraSecurity());
        }

        if(request.getAudioRecordSecurity() != null) {
            licenseType.setAudioRecordSecurity(request.getAudioRecordSecurity());
        }

        if(request.getScreenshotSecurity() != null) {
            licenseType.setScreenshotSecurity(request.getScreenshotSecurity());
        }

        if(request.getMouseTrackSecurity() != null) {
            licenseType.setMouseTrackSecurity(request.getMouseTrackSecurity());
        }

        return licenseType;
    }

    private void validateUniqueTypeName(String typeName) {
        boolean alreadyExistByName = licenseTypeRepository.findByTypeName(typeName.toUpperCase()).isPresent();
        if(alreadyExistByName) {
            throw new ResourceNotFoundException(PaymentServiceMessages.alreadyExists("LicenseType", "name", typeName));
        }
    }

    private void validateUniqueAmount(BigDecimal amount) {
        boolean alreadyExistByAmount = licenseTypeRepository.findByAmount(amount).isPresent();
        if(alreadyExistByAmount) {
            throw new ResourceNotFoundException(PaymentServiceMessages.alreadyExists("LicenseType", "amount", amount.toPlainString()));
        }
    }

}
