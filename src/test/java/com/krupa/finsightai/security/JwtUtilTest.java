package com.krupa.finsightai.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.krupa.finsightai.config.JwtUtil;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void generateAccessToken_ShouldReturnValidToken() {

        String token = jwtUtil.generateAccessToken("krupa");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void generateRefreshToken_ShouldReturnValidToken() {

        String token = jwtUtil.generateRefreshToken("krupa");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_ShouldReturnUsernameFromAccessToken() {

        String token = jwtUtil.generateAccessToken("krupa");

        String username = jwtUtil.extractUsername(token);

        assertEquals("krupa", username);
    }

    @Test
    void validateToken_ShouldReturnTrue_ForValidTokenAndUsername() {

        String token = jwtUtil.generateAccessToken("krupa");

        boolean valid =
                jwtUtil.validateToken(token, "krupa");

        assertTrue(valid);
    }

    @Test
    void validateToken_ShouldReturnFalse_ForWrongUsername() {

        String token = jwtUtil.generateAccessToken("krupa");

        boolean valid =
                jwtUtil.validateToken(token, "admin");

        assertFalse(valid);
    }

    @Test
    void refreshToken_ShouldContainCorrectUsername() {

        String token = jwtUtil.generateRefreshToken("krupa");

        String username = jwtUtil.extractUsername(token);

        assertEquals("krupa", username);
    }
}