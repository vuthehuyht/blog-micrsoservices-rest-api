package tech.vuthehuyht.blogrestapi.security;

import io.jsonwebtoken.Claims;

import java.security.Key;

public interface JwtService {
    Claims extractClaims(String token);

    Key getKey();

    String generateToken(CustomUserDetail customUserDetail);

    String refreshToken(CustomUserDetail customUserDetail);

    boolean isValidToken(String token);
}
