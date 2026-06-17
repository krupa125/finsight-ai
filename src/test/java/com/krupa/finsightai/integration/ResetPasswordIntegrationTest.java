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
class ResetPasswordIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldResetPassword() throws Exception {

        String username =
                "resetuser" + System.currentTimeMillis();

        String email =
                username + "@test.com";

        String registerRequest = """
                {
                  "name":"Reset User",
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

        String forgotRequest = """
                {
                  "email":"%s"
                }
                """.formatted(email);

        MvcResult forgotResult =
                mockMvc.perform(post("/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(forgotRequest))
                        .andExpect(status().isOk())
                        .andReturn();

        JsonNode forgotJson =
                objectMapper.readTree(
                        forgotResult.getResponse()
                                .getContentAsString());

        String token =
                forgotJson.get("data").asText();

        String resetRequest = """
                {
                  "token":"%s",
                  "newPassword":"NewPassword123"
                }
                """.formatted(token);

        mockMvc.perform(post("/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(resetRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("Password reset successful"));
    }
}