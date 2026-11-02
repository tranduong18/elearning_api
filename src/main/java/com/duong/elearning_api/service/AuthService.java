package com.duong.elearning_api.service;

import com.duong.elearning_api.dto.request.*;
import com.duong.elearning_api.dto.response.IntrospectResponse;
import com.duong.elearning_api.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);

    IntrospectResponse introspect(IntrospectRequest request);

    LoginResponse refreshToken(RefreshTokenRequest request);

    void logout(LogoutRequest request);

    void changePassword(String userId, ChangePasswordRequest request);
}
