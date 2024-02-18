package com.senpare.examservice.client;

import com.senpare.examservice.client.response.License;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("payment-service")
public interface LicenseClient {

    @GetMapping("/api/v1/payment-service/licenses/{uuid}")
    License getLicense(@RequestParam("uuid") String uuid);

    @GetMapping("/api/v1/payment-service/licenses/validate-by-uuid/{uuid}")
    boolean validateLicense(@RequestParam("uuid") String uuid);

    @PutMapping("/api/v1/payment-service/licenses/use/{uuid}")
    License useLicense(@RequestParam("uuid") String uuid);

}
