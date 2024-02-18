package com.senpare.authservice.controller;

import com.senpare.authservice.dto.AuthRequest;
import com.senpare.authservice.exception.BadCredentialsException;
import com.senpare.authservice.security.JwtService;
import com.senpare.authservice.utilities.AuthServiceMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth-service/token")
@Slf4j
@Tag(name = "Authentication endpoint", description = "Handles authentication and authorization")
public class AuthController {


    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    @PostMapping
    @Operation(summary = "Get token to login into the system", description = "Returns access and refresh tokens for the given username and password.")
    public ResponseEntity<Map<String, String>> getToken(@RequestBody AuthRequest authRequest, HttpServletRequest request) {
        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch(Throwable t) {
            throw new BadCredentialsException(AuthServiceMessages.invalidCredentials());
        }

        List<String> roles = authenticate.getAuthorities()
                .stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.toList());

        Map<String, String> tokens = jwtService.generateToken(authRequest.getUsername(), roles, request.getRequestURL().toString());
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/refresh")
    @Operation(summary = "Get refresh token", description = "Returns new access and refresh tokens for the given refresh token in the authorization header.")
    public ResponseEntity<Map<String, String>> refreshToken(HttpServletRequest request) {
        String refreshToken = jwtService.resolveToken(request);
        Map<String, String> tokens = jwtService.refreshToken(refreshToken, request.getRequestURL().toString());
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/validate")
    @Operation(summary = "Validate token", description = "Returns true if the given token is valid.")
    public ResponseEntity<Boolean> validateToken(@RequestParam("token") String token) {
        jwtService.validateToken(token);
        return ResponseEntity.ok(true);
    }
}
