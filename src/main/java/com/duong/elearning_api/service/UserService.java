package com.duong.elearning_api.service;

import com.duong.elearning_api.dto.request.CreateUserRequest;
import com.duong.elearning_api.dto.response.CreateUserResponse;
import com.duong.elearning_api.dto.response.UserProfileResponse;

public interface UserService {
    CreateUserResponse createUser(CreateUserRequest request);

    UserProfileResponse getMyProfile(String userId);
}
