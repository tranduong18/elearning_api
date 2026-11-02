package com.duong.elearning_api.controller;

import com.duong.elearning_api.dto.request.IntrospectRequest;
import com.duong.elearning_api.dto.request.LoginRequest;
import com.duong.elearning_api.dto.request.RefreshTokenRequest;
import com.duong.elearning_api.dto.response.ApiResponse;
import com.duong.elearning_api.dto.response.IntrospectResponse;
import com.duong.elearning_api.dto.response.LoginResponse;
import com.duong.elearning_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
}
