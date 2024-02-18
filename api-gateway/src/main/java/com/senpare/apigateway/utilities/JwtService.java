package com.senpare.apigateway.utilities;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.senpare.apigateway.exception.BadAuthorizationRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtService {

    private final String secret;

    public JwtService(@Value("${app.jwt.secrete}") String secret) {
        this.secret = secret;
    }

    public Map<String, Claim> getAllClaimsFromToken(String token) {
        return decodeToken(token).getClaims();
    }

    private boolean isTokenExpired(String token) {
        return decodeToken(token).getExpiresAt().before(new Date());
    }

    public boolean isInvalid(String token) {
        return isTokenExpired(token);
    }

    public DecodedJWT decodeToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();

        try {
            return verifier.verify(token);
        } catch (Throwable t) {
            throw new BadAuthorizationRequestException(t.getMessage());
        }
    }

}