package com.senpare.paymentservice.service;

import com.senpare.paymentservice.dto.LicenseRequest;
import com.senpare.paymentservice.dto.LicenseStatus;
import com.senpare.paymentservice.exception.BadRequestException;
import com.senpare.paymentservice.exception.ResourceNotFoundException;
import com.senpare.paymentservice.model.License;
import com.senpare.paymentservice.model.LicenseType;
import com.senpare.paymentservice.repository.LicenseRepository;
import com.senpare.paymentservice.utilities.PaymentServiceMessages;
import com.senpare.paymentservice.utilities.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LicenseService {

    private final LicenseRepository licenseRepository;
    private final LicenseTypeService licenseTypeService;

    public License create(LicenseRequest request) {
        if(request == null) {
            throw new BadRequestException("LicenseRequest");
        }

        License license = licenseFromRequest(request);

        LicenseType licenseType = licenseTypeService.getByAmount(request.getAmount());
        license.setLicenseType(licenseType);

        return licenseRepository.save(license);
    }

    public License get(UUID uuid) {
        if(uuid == null) {
            throw new BadRequestException("License.uuid");
        }

        return licenseRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(PaymentServiceMessages.canNotBeFound("License", "uuid", uuid.toString())));
    }

    public License getByLicenseKey(UUID licenseKey) {
        if(licenseKey == null) {
            throw new BadRequestException("License.licenseKey");
        }

        return licenseRepository.findByLicenseKey(licenseKey)
                .orElseThrow(() -> new ResourceNotFoundException(PaymentServiceMessages.canNotBeFound("License", "licenseKey", licenseKey.toString())));
    }

    public Page<License> getAll(int page, int size, String sortBy) {
        return Util.paginate(licenseRepository, page, size, sortBy);
    }

    public Page<License> getAllByEmail(String email, String licenseStatus, int page, int size, String sortBy) {
        if(Util.isNullOrEmpty(email)) {
            throw new BadRequestException("License.email");
        }

        if(Util.isNotNullAndEmpty(licenseStatus)) {
            return licenseRepository.findAllByEmailAndLicenseStatus(email, LicenseStatus.lookupByCode(licenseStatus), Util.getPageable(page, size, sortBy));
        }

        return licenseRepository.findAllByEmail(email, Util.getPageable(page, size, sortBy));
    }

    public License useLicense(UUID uuid) {
        if(uuid == null) {
            throw new BadRequestException("License.uuid");
        }

        License license = licenseRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(PaymentServiceMessages.canNotBeFound("License", "uuid", uuid.toString())));

        license.setLicenseStatus(LicenseStatus.USED);

        return licenseRepository.save(license);
    }

    public License delete(UUID uuid) {
        if(uuid == null) {
            throw new BadRequestException("License.uuid");
        }

        License license = licenseRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(PaymentServiceMessages.canNotBeFound("License", "uuid", uuid.toString())));


        licenseRepository.delete(license);

        return license;
    }

    public boolean isLicenseKeyValid(UUID licenseKey) {
        if(licenseKey == null) {
            throw new BadRequestException("License.licenseKey");
        }

        Optional<License> license = licenseRepository.findByLicenseKey(licenseKey);

        return license.isPresent() && (license.get().getLicenseStatus() == LicenseStatus.NEW);
    }

    public boolean isLicenseValid(UUID uuid) {
        if(uuid == null) {
            throw new BadRequestException("License.uuid");
        }

        Optional<License> license = licenseRepository.findById(uuid);

        return license.isPresent() && (license.get().getLicenseStatus() == LicenseStatus.NEW);
    }

    public License licenseFromRequest(LicenseRequest request) {
        return new License()
                .setUuid(UUID.randomUUID())
                .setEmail(request.getEmail())
                .setLicenseKey(UUID.randomUUID())
                .setLicenseType(licenseTypeService.getByAmount(request.getAmount()))
                .setLicenseStatus(LicenseStatus.NEW);
    }


}
