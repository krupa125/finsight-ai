package com.krupa.finsightai.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {


private final AuthenticationManager authenticationManager;
private final UserService userService;
private final JwtUtil jwtUtil;
private final PasswordResetService passwordResetService;
private final RefreshTokenService refreshTokenService;

public AuthController(
        AuthenticationManager authenticationManager,
        UserService userService,
        JwtUtil jwtUtil,
        PasswordResetService passwordResetService,
        RefreshTokenService refreshTokenService) {

    this.authenticationManager = authenticationManager;
    this.userService = userService;
    this.jwtUtil = jwtUtil;
    this.passwordResetService = passwordResetService;
    this.refreshTokenService = refreshTokenService;
}

@Operation(
        summary = "Register User",
        description = "Creates a new user account"
)
@ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "User registered successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "Invalid request")
})
@PostMapping("/register")
public ApiResponse<User> register(
        @Valid @RequestBody RegisterRequest request) {

    User user = new User();

    user.setName(request.getName());
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    user.setPassword(request.getPassword());
    user.setBalance(request.getBalance());

    return new ApiResponse<>(
            true,
            "User registered successfully",
            userService.registerUser(user)
    );
}

@Operation(
        summary = "Login User",
        description = "Authenticates user and returns JWT access token and refresh token"
)
@ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "Login successful"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "Invalid credentials")
})
@PostMapping("/login")
public ApiResponse<AuthResponse> login(
        @RequestBody AuthRequest request) {

    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()));

    String accessToken =
            jwtUtil.generateAccessToken(
                    request.getUsername());

    User user =
            userService.getUserByUsername(
                    request.getUsername());

    RefreshToken refreshToken =
            refreshTokenService.createRefreshToken(user);

    return new ApiResponse<>(
            true,
            "Login successful",
            new AuthResponse(
                    accessToken,
                    refreshToken.getToken())
    );
}

@Operation(
        summary = "Refresh Access Token",
        description = "Generates a new access token using a valid refresh token"
)
@ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "Token refreshed successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "Refresh token invalid or expired")
})
@PostMapping("/refresh")
public ApiResponse<AuthResponse> refreshToken(
        @RequestBody RefreshTokenRequest request) {

    RefreshToken refreshToken =
            refreshTokenService.verifyRefreshToken(
                    request.getRefreshToken());

    String newAccessToken =
            jwtUtil.generateAccessToken(
                    refreshToken.getUser()
                            .getUsername());

    return new ApiResponse<>(
            true,
            "Token refreshed successfully",
            new AuthResponse(
                    newAccessToken,
                    refreshToken.getToken())
    );
}

@Operation(
        summary = "Logout User",
        description = "Deletes refresh token and logs user out"
)
@ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "Logout successful")
})
@PostMapping("/logout")
public ApiResponse<String> logout(
        @RequestBody RefreshTokenRequest request) {

    refreshTokenService.deleteByToken(
            request.getRefreshToken());

    return new ApiResponse<>(
            true,
            "Logged out successfully",
            null
    );
}

@Operation(
        summary = "Forgot Password",
        description = "Generates password reset token"
)
@PostMapping("/forgot-password")
public ApiResponse<String> forgotPassword(
        @RequestBody ForgotPasswordRequest request) {

    String token =
            passwordResetService.createResetToken(
                    request.getEmail());

    return new ApiResponse<>(
            true,
            "Reset token generated",
            token
    );
}

@Operation(
        summary = "Reset Password",
        description = "Resets user password using reset token"
)
@PostMapping("/reset-password")
public ApiResponse<String> resetPassword(
        @RequestBody ResetPasswordRequest request) {

    passwordResetService.resetPassword(
            request.getToken(),
            request.getNewPassword());

    return new ApiResponse<>(
            true,
            "Password reset successful",
            null
    );
}


}
