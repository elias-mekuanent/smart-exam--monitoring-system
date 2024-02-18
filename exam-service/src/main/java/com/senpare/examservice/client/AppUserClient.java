package com.senpare.examservice.client;

import com.senpare.examservice.client.response.AppUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient("auth-service")
public interface AppUserClient {

    @GetMapping("/api/v1/auth-service/profile")
    AppUser getAppUser();

    @GetMapping("/api/v1/auth-service/users/{uuid}")
    AppUser getAppUserByUuid(@PathVariable UUID uuid);

    @GetMapping("/api/v1/auth-service/users/examinees")
    List<AppUser> getExaminees(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam (value =  "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false, defaultValue = "firstName") String sort,
            @RequestParam("examUuid") String examUuid);

}
