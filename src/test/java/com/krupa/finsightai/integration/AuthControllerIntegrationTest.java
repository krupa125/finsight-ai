package com.krupa.finsightai.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.krupa.finsightai.model.User;
import com.krupa.finsightai.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        assertNotNull(userRepository);
    }

    @Test
    void userRepository_ShouldSaveAndFindUser() {

        String uniqueUsername =
                "test_" + System.currentTimeMillis();

        String uniqueEmail =
                uniqueUsername + "@test.com";

        User user = new User();
        user.setName("Krupa");
        user.setUsername(uniqueUsername);
        user.setEmail(uniqueEmail);
        user.setPassword("password");

        userRepository.save(user);

        assertTrue(
                userRepository.findByUsername(uniqueUsername)
                        .isPresent()
        );

        assertTrue(
                userRepository.findByEmail(uniqueEmail)
                        .isPresent()
        );
    }
}