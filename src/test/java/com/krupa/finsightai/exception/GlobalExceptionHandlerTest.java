package com.krupa.finsightai.exception;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleNotFound_ShouldReturn404() {

        ResourceNotFoundException ex =
                new ResourceNotFoundException("User not found");

        ResponseEntity<Map<String, Object>> response =
                handler.handleNotFound(ex);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Not Found", response.getBody().get("error"));
        assertEquals("User not found",
                response.getBody().get("message"));
    }

    @Test
    void handleIllegalArgument_ShouldReturn400() {

        IllegalArgumentException ex =
                new IllegalArgumentException("Invalid input");

        ResponseEntity<Map<String, Object>> response =
                handler.handleIllegalArgument(ex);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Bad Request",
                response.getBody().get("error"));
    }

    @Test
    void handleBadCredentials_ShouldReturn401() {

        BadCredentialsException ex =
                new BadCredentialsException("Bad credentials");

        ResponseEntity<Map<String, Object>> response =
                handler.handleBadCredentials(ex);

        assertEquals(401, response.getStatusCode().value());
        assertEquals("Unauthorized",
                response.getBody().get("error"));
        assertEquals(
                "Invalid username or password",
                response.getBody().get("message")
        );
    }

    @Test
    void handleGeneral_ShouldReturn500() {

        Exception ex =
                new Exception("Unexpected error");

        ResponseEntity<Map<String, Object>> response =
                handler.handleGeneral(ex);

        assertEquals(500, response.getStatusCode().value());
        assertEquals(
                "Internal Server Error",
                response.getBody().get("error")
        );
    }

    @Test
    void resourceNotFoundException_ShouldStoreMessage() {

        ResourceNotFoundException ex =
                new ResourceNotFoundException("Test message");

        assertEquals("Test message", ex.getMessage());
    }
}