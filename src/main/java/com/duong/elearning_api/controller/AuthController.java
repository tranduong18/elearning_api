package com.duong.elearning_api.controller;

import com.duong.elearning_api.dto.request.*;
import com.duong.elearning_api.dto.response.ApiResponse;
import com.duong.elearning_api.dto.response.IntrospectResponse;
import com.duong.elearning_api.dto.response.LoginResponse;
import com.duong.elearning_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        LoginResponse data = authService.login(request);

        return ApiResponse.<LoginResponse>builder()
                .code(200)
                .message("Login successfully")
                .data(data)
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request){
        LoginResponse data = authService.refreshToken(request);
        return ApiResponse.<LoginResponse>builder()
                .code(200)
                .message("Refresh token successfully")
                .data(data)
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@Valid @RequestBody IntrospectRequest request){
        IntrospectResponse data = authService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .code(200)
                .message("Introspect token successfully")
                .data(data)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@Valid @RequestBody LogoutRequest request){
        authService.logout(request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Logout successfully")
                .build();
    }

    @PostMapping("/change-password")
    public ApiResponse<Void> changePassword(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ChangePasswordRequest request
    ){
        String userId = jwt.getSubject();
        authService.changePassword(userId, request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Change password successfully")
                .build();
    }
}
