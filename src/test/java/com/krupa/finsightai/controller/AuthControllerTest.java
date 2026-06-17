package com.krupa.finsightai.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;

import com.krupa.finsightai.config.JwtUtil;
import com.krupa.finsightai.dto.ApiResponse;
import com.krupa.finsightai.dto.AuthRequest;
import com.krupa.finsightai.dto.AuthResponse;
import com.krupa.finsightai.dto.ForgotPasswordRequest;
import com.krupa.finsightai.dto.RefreshTokenRequest;
import com.krupa.finsightai.dto.RegisterRequest;
import com.krupa.finsightai.dto.ResetPasswordRequest;
import com.krupa.finsightai.model.RefreshToken;
import com.krupa.finsightai.model.User;
import com.krupa.finsightai.service.PasswordResetService;
import com.krupa.finsightai.service.RefreshTokenService;
import com.krupa.finsightai.service.UserService;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordResetService passwordResetService;

    @Mock
    private RefreshTokenService refreshTokenService;

    private AuthController authController;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        authController = new AuthController(
                authenticationManager,
                userService,
                jwtUtil,
                passwordResetService,
                refreshTokenService
        );
    }

    @Test
    void register_ShouldReturnSuccessResponse() {

        RegisterRequest request = new RegisterRequest();
        request.setName("Krupa");
        request.setUsername("krupa");
        request.setEmail("krupa@test.com");
        request.setPassword("password");
        request.setBalance(1000.0);

        User savedUser = new User();
        savedUser.setUsername("krupa");

        when(userService.registerUser(any(User.class)))
                .thenReturn(savedUser);

        ApiResponse<User> response =
                authController.register(request);

        assertTrue(response.isSuccess());
        assertEquals(
                "User registered successfully",
                response.getMessage()
        );
    }

    @Test
    void login_ShouldReturnTokens() {

        AuthRequest request = new AuthRequest();
        request.setUsername("krupa");
        request.setPassword("password");

        User user = new User();
        user.setUsername("krupa");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-token");
        refreshToken.setUser(user);

        when(jwtUtil.generateAccessToken("krupa"))
                .thenReturn("access-token");

        when(userService.getUserByUsername("krupa"))
                .thenReturn(user);

        when(refreshTokenService.createRefreshToken(user))
                .thenReturn(refreshToken);

        ApiResponse<AuthResponse> response =
                authController.login(request);

        assertTrue(response.isSuccess());
        assertEquals(
                "Login successful",
                response.getMessage()
        );

        assertEquals(
                "access-token",
                response.getData().getAccessToken()
        );

        assertEquals(
                "refresh-token",
                response.getData().getRefreshToken()
        );
    }

    @Test
    void refreshToken_ShouldReturnNewAccessToken() {

        User user = new User();
        user.setUsername("krupa");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-token");
        refreshToken.setUser(user);

        RefreshTokenRequest request =
                new RefreshTokenRequest();
        request.setRefreshToken("refresh-token");

        when(refreshTokenService.verifyRefreshToken("refresh-token"))
                .thenReturn(refreshToken);

        when(jwtUtil.generateAccessToken("krupa"))
                .thenReturn("new-access-token");

        ApiResponse<AuthResponse> response =
                authController.refreshToken(request);

        assertTrue(response.isSuccess());
        assertEquals(
                "Token refreshed successfully",
                response.getMessage()
        );
    }

    @Test
    void logout_ShouldDeleteRefreshToken() {

        RefreshTokenRequest request =
                new RefreshTokenRequest();

        request.setRefreshToken("refresh-token");

        ApiResponse<String> response =
                authController.logout(request);

        verify(refreshTokenService)
                .deleteByToken("refresh-token");

        assertTrue(response.isSuccess());
        assertEquals(
                "Logged out successfully",
                response.getMessage()
        );
    }

    @Test
    void forgotPassword_ShouldReturnResetToken() {

        ForgotPasswordRequest request =
                new ForgotPasswordRequest();

        request.setEmail("krupa@test.com");

        when(passwordResetService.createResetToken(
                "krupa@test.com"))
                .thenReturn("reset-token");

        ApiResponse<String> response =
                authController.forgotPassword(request);

        assertTrue(response.isSuccess());
        assertEquals(
                "reset-token",
                response.getData()
        );
    }

    @Test
    void resetPassword_ShouldReturnSuccess() {

        ResetPasswordRequest request =
                new ResetPasswordRequest();

        request.setToken("token");
        request.setNewPassword("newPassword");

        ApiResponse<String> response =
                authController.resetPassword(request);

        verify(passwordResetService)
                .resetPassword(
                        "token",
                        "newPassword"
                );

        assertTrue(response.isSuccess());
        assertEquals(
                "Password reset successful",
                response.getMessage()
        );
    }
}