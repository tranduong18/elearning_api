package com.duong.elearning_api.service;

import com.duong.elearning_api.dto.request.LoginRequest;
import com.duong.elearning_api.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
