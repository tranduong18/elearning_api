package com.duong.elearning_api.service;

import com.duong.elearning_api.dto.request.CreateUserRequest;
import com.duong.elearning_api.dto.response.CreateUserResponse;

public interface UserService {
    CreateUserResponse createUser(CreateUserRequest request);
}
