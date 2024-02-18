package com.senpare.paymentservice.utilities;

import com.senpare.paymentservice.dto.LicenseTypeRequest;
import com.senpare.paymentservice.model.LicenseType;
import com.senpare.paymentservice.service.LicenseTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Component
public class DbSeeder implements ApplicationListener<ApplicationReadyEvent> {

    private static boolean alreadySetup = false;

    private final LicenseTypeService licenseTypeService;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Page<LicenseType> licenseTypes = licenseTypeService.getAll(1, 1, null);

        if (alreadySetup || licenseTypes.getTotalElements() > 0) {
            return;
        }

        LicenseTypeRequest basicLicense = new LicenseTypeRequest()
                .setTypeName("BASIC")
                .setAmount(new BigDecimal("1000"))
                .setScreenshotSecurity(true)
                .setCameraSecurity(false)
                .setMouseTrackSecurity(false)
                .setAudioRecordSecurity(false)
                .setAllowedExamineeCount(50)
                ;

        LicenseTypeRequest teamLicense = new LicenseTypeRequest()
                .setTypeName("TEAM")
                .setAmount(new BigDecimal("10000"))
                .setScreenshotSecurity(true)
                .setCameraSecurity(true)
                .setMouseTrackSecurity(false)
                .setAudioRecordSecurity(false)
                .setAllowedExamineeCount(100)
                ;

        LicenseTypeRequest businessLicense = new LicenseTypeRequest()
                .setTypeName("BUSINESS")
                .setAmount(new BigDecimal("20000"))
                .setScreenshotSecurity(true)
                .setCameraSecurity(true)
                .setMouseTrackSecurity(true)
                .setAudioRecordSecurity(false)
                .setAllowedExamineeCount(1000)
                ;

        LicenseTypeRequest enterpriseLicense = new LicenseTypeRequest()
                .setTypeName("ENTERPRISE")
                .setAmount(new BigDecimal("500000"))
                .setScreenshotSecurity(true)
                .setCameraSecurity(true)
                .setMouseTrackSecurity(true)
                .setAudioRecordSecurity(true)
                .setAllowedExamineeCount(10000)
                ;

        licenseTypeService.create(basicLicense);
        licenseTypeService.create(teamLicense);
        licenseTypeService.create(businessLicense);
        licenseTypeService.create(enterpriseLicense);

        alreadySetup = true;

    }
}
