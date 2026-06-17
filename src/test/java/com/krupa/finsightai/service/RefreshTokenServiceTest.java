package com.krupa.finsightai.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.krupa.finsightai.model.RefreshToken;
import com.krupa.finsightai.model.User;
import com.krupa.finsightai.repository.RefreshTokenRepository;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    void createRefreshToken_ShouldCreateTokenSuccessfully() {

        User user = new User();
        user.setId(1L);
        user.setUsername("john");

        when(refreshTokenRepository.save(any(RefreshToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        assertNotNull(refreshToken);

        assertNotNull(refreshToken.getToken());

        assertEquals(user, refreshToken.getUser());

        assertTrue(
                refreshToken.getExpiryDate()
                        .isAfter(java.time.LocalDateTime.now())
        );

        verify(refreshTokenRepository)
                .deleteByUser(user);

        verify(refreshTokenRepository)
                .save(any(RefreshToken.class));
    }

    @Test
void verifyRefreshToken_ShouldReturnToken_WhenTokenIsValid() {

    RefreshToken refreshToken = new RefreshToken();

    refreshToken.setToken("valid-token");

    refreshToken.setExpiryDate(
            java.time.LocalDateTime.now().plusDays(1)
    );

    when(refreshTokenRepository.findByToken("valid-token"))
            .thenReturn(java.util.Optional.of(refreshToken));

    RefreshToken result =
            refreshTokenService.verifyRefreshToken("valid-token");

    assertNotNull(result);

    assertEquals(
            "valid-token",
            result.getToken()
    );

    verify(refreshTokenRepository)
            .findByToken("valid-token");

    verify(refreshTokenRepository, never())
            .delete(any(RefreshToken.class));
    }

    @Test
void verifyRefreshToken_ShouldThrowException_WhenTokenNotFound() {

    when(refreshTokenRepository.findByToken("invalid-token"))
            .thenReturn(java.util.Optional.empty());

    RuntimeException exception =
            assertThrows(
                    RuntimeException.class,
                    () -> refreshTokenService
                            .verifyRefreshToken("invalid-token")
            );

    assertEquals(
            "Refresh token not found",
            exception.getMessage()
    );

    verify(refreshTokenRepository)
            .findByToken("invalid-token");

    verify(refreshTokenRepository, never())
            .delete(any(RefreshToken.class));
    }

    @Test
void verifyRefreshToken_ShouldThrowException_WhenTokenExpired() {

    RefreshToken refreshToken = new RefreshToken();

    refreshToken.setToken("expired-token");

    refreshToken.setExpiryDate(
            java.time.LocalDateTime.now().minusDays(1)
    );

    when(refreshTokenRepository.findByToken("expired-token"))
            .thenReturn(java.util.Optional.of(refreshToken));

    RuntimeException exception =
            assertThrows(
                    RuntimeException.class,
                    () -> refreshTokenService
                            .verifyRefreshToken("expired-token")
            );

    assertEquals(
            "Refresh token expired",
            exception.getMessage()
    );

    verify(refreshTokenRepository)
            .findByToken("expired-token");

    verify(refreshTokenRepository)
            .delete(refreshToken);
    }

    @Test
void deleteByToken_ShouldDeleteTokenSuccessfully() {

    refreshTokenService.deleteByToken("token-123");

    verify(refreshTokenRepository)
            .deleteByToken("token-123");
    }


}