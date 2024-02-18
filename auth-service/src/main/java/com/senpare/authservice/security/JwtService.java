package com.senpare.authservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.senpare.authservice.exception.BadRequestException;
import com.senpare.authservice.model.AppUser;
import com.senpare.authservice.security.AppUserDetails;
import com.senpare.authservice.service.AppUserService;
import com.senpare.authservice.utilities.AuthServiceMessages;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final AppUserService appUserService;
    private final String secreteKey;
    private final int accessTokenExpirationMinutes;
    private final int refreshTokenExpirationMinutes;

    public JwtService(AppUserService appUserService,
                      @Value("${app.jwt.secrete}") String secreteKey,
                      @Value("${app.jwt.expiration-minutes.access-token}") int accessTokenExpirationMinutes,
                      @Value("${app.jwt.expiration-minutes.refresh-token}") int refreshTokenExpirationMinutes) {
        this.appUserService = appUserService;
        this.secreteKey = secreteKey;
        this.accessTokenExpirationMinutes = accessTokenExpirationMinutes;
        this.refreshTokenExpirationMinutes = refreshTokenExpirationMinutes;
    }

    public DecodedJWT validateToken(final String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secreteKey.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new BadRequestException(e.getMessage());
        }

    }

    public Map<String, String> generateToken(String username, List<String> roles, String issuerUrl) {
        Algorithm algorithm = Algorithm.HMAC256(secreteKey.getBytes());
        String accessToken = JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + (accessTokenExpirationMinutes * 60 * 1000)))
                .withIssuer(issuerUrl)
                .withClaim("roles", roles)
                .sign(algorithm);
        String refreshToken = JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + (refreshTokenExpirationMinutes * 60 * 1000)))
                .withIssuer(issuerUrl)
                .sign(algorithm);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        return tokens;
    }

    public Map<String, String> refreshToken(String refreshToken, String issuerUrl) {
        Algorithm algorithm = Algorithm.HMAC256(secreteKey.getBytes());
        DecodedJWT decodedJWT = validateToken(refreshToken);

        String username = decodedJWT.getSubject();
        AppUser appUser = appUserService.getAppUserByEmail(username);
        AppUserDetails appUserDetails = new AppUserDetails(appUser);
        String accessToken = JWT.create()
                .withSubject(appUser.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + (accessTokenExpirationMinutes * 60 * 1000)))
                .withIssuer(issuerUrl)
                .withClaim("roles", appUserDetails.getAuthorities()
                        .stream()
                        .map(authority -> authority.getAuthority())
                        .collect(Collectors.toList()))
                .sign(algorithm);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        return tokens;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNullOrEmpty("Authorization token"));
        }

        return bearerToken.substring(7);
    }

}
