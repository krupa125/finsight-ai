package com.krupa.finsightai.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.krupa.finsightai.repository.RefreshTokenRepository;
import com.krupa.finsightai.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthLoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void cleanup() {
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void login_ShouldReturnAccessTokenAndRefreshToken() throws Exception {

        String username =
                "user" + System.currentTimeMillis();

        String email =
                username + "@test.com";

        String registerRequest = """
                {
                    "name":"Krupa",
                    "username":"%s",
                    "email":"%s",
                    "password":"password123",
                    "balance":1000
                }
                """.formatted(username, email);

        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest)
        )
                .andExpect(status().isOk());

        String loginRequest = """
                {
                    "username":"%s",
                    "password":"password123"
                }
                """.formatted(username);

        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists());
    }
}