package com.hoehn.game.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private static final String TEST_SECRET =
            "this-is-a-test-secret-that-is-long-enough-for-hmac";

    @Test
    void generateToken() {
        JwtService jwtService = new JwtService(TEST_SECRET);

        String token = jwtService.generateToken("testUser");

        assertNotNull(token);
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void generateTokenContainsUsername() {
        JwtService jwtService = new JwtService(TEST_SECRET);

        String token = jwtService.generateToken("testUser");

        SecretKey key = Keys.hmacShaKeyFor(
                TEST_SECRET.getBytes(StandardCharsets.UTF_8)
        );

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertEquals("testUser", claims.getSubject());
    }

    @Test
    void generateTokenHasExpiration() {
        JwtService jwtService = new JwtService(TEST_SECRET);

        String token = jwtService.generateToken("testUser");

        SecretKey key = Keys.hmacShaKeyFor(
                TEST_SECRET.getBytes(StandardCharsets.UTF_8)
        );

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertNotNull(claims.getExpiration());
        assertTrue(claims.getExpiration().after(claims.getIssuedAt()));
    }
}