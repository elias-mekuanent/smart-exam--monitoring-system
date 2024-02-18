package com.senpare.testservice;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
@RequestMapping("/api/v1/test-service")
@Tag(name = "TestController", description = "An endpoint created for testing purposes")
public class TestController {

    @GetMapping("/public")
    @Operation(summary = "A public api that doesn't need authentication")
    public String publicApi() {
        return "public api";
    }

    @GetMapping("/private")
    @Operation(summary = "A private api secured by OAuth 2.0 + OIDC")
    public String privateApi(Principal principal) {
        System.out.println("Principal Name: " + principal.getName());
        return "private api";
    }
}
