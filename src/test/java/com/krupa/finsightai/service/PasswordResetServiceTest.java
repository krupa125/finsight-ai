package com.krupa.finsightai.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.krupa.finsightai.model.PasswordResetToken;
import com.krupa.finsightai.model.User;
import com.krupa.finsightai.repository.PasswordResetTokenRepository;
import com.krupa.finsightai.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordResetService passwordResetService;

    @Test
void createResetToken_ShouldCreateTokenSuccessfully() {

    User user = new User();
    user.setId(1L);
    user.setEmail("john@test.com");

    when(userRepository.findByEmail("john@test.com"))
            .thenReturn(Optional.of(user));

    when(tokenRepository.save(any(PasswordResetToken.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    String token =
            passwordResetService.createResetToken("john@test.com");

    assertNotNull(token);

    verify(userRepository)
            .findByEmail("john@test.com");

    verify(tokenRepository)
            .save(any(PasswordResetToken.class));
    }

    @Test
void createResetToken_ShouldThrowException_WhenUserNotFound() {

    when(userRepository.findByEmail("unknown@test.com"))
            .thenReturn(Optional.empty());

    RuntimeException exception =
            assertThrows(
                    RuntimeException.class,
                    () -> passwordResetService
                            .createResetToken("unknown@test.com")
            );

    assertEquals(
            "User not found",
            exception.getMessage()
    );

    verify(userRepository)
            .findByEmail("unknown@test.com");

    verify(tokenRepository, never())
            .save(any(PasswordResetToken.class));
    }

    @Test
void resetPassword_ShouldResetPasswordSuccessfully() {

    User user = new User();
    user.setId(1L);
    user.setEmail("john@test.com");

    PasswordResetToken resetToken =
            new PasswordResetToken();

    resetToken.setToken("valid-token");
    resetToken.setUser(user);

    resetToken.setExpiryDate(
            java.time.LocalDateTime.now().plusMinutes(10)
    );

    when(tokenRepository.findByToken("valid-token"))
            .thenReturn(Optional.of(resetToken));

    when(passwordEncoder.encode("newPassword"))
            .thenReturn("encodedPassword");

    passwordResetService.resetPassword(
            "valid-token",
            "newPassword"
    );

    assertEquals(
            "encodedPassword",
            user.getPassword()
    );

    verify(tokenRepository)
            .findByToken("valid-token");

    verify(passwordEncoder)
            .encode("newPassword");

    verify(userRepository)
            .save(user);

    verify(tokenRepository)
            .delete(resetToken);
    }

    @Test
void resetPassword_ShouldThrowException_WhenTokenIsInvalid() {

    when(tokenRepository.findByToken("invalid-token"))
            .thenReturn(Optional.empty());

    RuntimeException exception =
            assertThrows(
                    RuntimeException.class,
                    () -> passwordResetService.resetPassword(
                            "invalid-token",
                            "newPassword"
                    )
            );

    assertEquals(
            "Invalid token",
            exception.getMessage()
    );

    verify(tokenRepository)
            .findByToken("invalid-token");

    verify(userRepository, never())
            .save(any(User.class));

    verify(tokenRepository, never())
            .delete(any(PasswordResetToken.class));
    }

    @Test
void resetPassword_ShouldThrowException_WhenTokenExpired() {

    User user = new User();

    PasswordResetToken resetToken =
            new PasswordResetToken();

    resetToken.setToken("expired-token");
    resetToken.setUser(user);

    resetToken.setExpiryDate(
            java.time.LocalDateTime.now().minusMinutes(1)
    );

    when(tokenRepository.findByToken("expired-token"))
            .thenReturn(Optional.of(resetToken));

    RuntimeException exception =
            assertThrows(
                    RuntimeException.class,
                    () -> passwordResetService.resetPassword(
                            "expired-token",
                            "newPassword"
                    )
            );

    assertEquals(
            "Token expired",
            exception.getMessage()
    );

    verify(tokenRepository)
            .findByToken("expired-token");

    verify(userRepository, never())
            .save(any(User.class));

    verify(tokenRepository, never())
            .delete(any(PasswordResetToken.class));
    }

}