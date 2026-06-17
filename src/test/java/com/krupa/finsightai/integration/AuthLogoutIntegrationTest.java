package com.krupa.finsightai.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthLogoutIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldLogoutUser() throws Exception {

        String username =
                "logoutuser" + System.currentTimeMillis();

        String email =
                username + "@test.com";

        String registerRequest = """
                {
                  "name":"Logout User",
                  "username":"%s",
                  "email":"%s",
                  "password":"Password123",
                  "balance":1000
                }
                """.formatted(username, email);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequest))
                .andExpect(status().isOk());

        String loginRequest = """
                {
                  "username":"%s",
                  "password":"Password123"
                }
                """.formatted(username);

        MvcResult loginResult =
                mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                        .andExpect(status().isOk())
                        .andReturn();

        JsonNode loginJson =
                objectMapper.readTree(
                        loginResult.getResponse()
                                .getContentAsString());

        String refreshToken =
                loginJson.get("data")
                        .get("refreshToken")
                        .asText();

        String logoutRequest = """
                {
                  "refreshToken":"%s"
                }
                """.formatted(refreshToken);

        mockMvc.perform(post("/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(logoutRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("Logged out successfully"));
    }
}