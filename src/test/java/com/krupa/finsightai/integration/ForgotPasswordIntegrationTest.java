package com.krupa.finsightai.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ForgotPasswordIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGenerateResetToken() throws Exception {

        String username =
                "forgotuser" + System.currentTimeMillis();

        String email =
                username + "@test.com";

        String registerRequest = """
                {
                  "name":"Forgot User",
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

        mockMvc.perform(post("/auth/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(forgotRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isString());
    }
}