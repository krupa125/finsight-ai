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
class AuthRefreshIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
void shouldRefreshAccessToken() throws Exception {

    String username =
            "refreshuser" + System.currentTimeMillis();

    String email =
            username + "@test.com";

    String registerRequest = """
            {
              "name":"Refresh User",
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

    String loginResponse =
            loginResult.getResponse().getContentAsString();

    JsonNode loginJson =
            objectMapper.readTree(loginResponse);

    String refreshToken =
            loginJson.get("data")
                     .get("refreshToken")
                     .asText();

    String refreshRequest = """
            {
              "refreshToken":"%s"
            }
            """.formatted(refreshToken);

    mockMvc.perform(post("/auth/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .content(refreshRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.accessToken").exists())
            .andExpect(jsonPath("$.data.refreshToken").exists());
    }
}