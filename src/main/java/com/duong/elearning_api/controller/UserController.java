package com.duong.elearning_api.controller;

import com.duong.elearning_api.dto.request.CreateUserRequest;
import com.duong.elearning_api.dto.response.ApiResponse;
import com.duong.elearning_api.dto.response.CreateUserResponse;
import com.duong.elearning_api.dto.response.UserProfileResponse;
import com.duong.elearning_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ApiResponse<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest request){
        CreateUserResponse data = userService.createUser(request);
        return ApiResponse.<CreateUserResponse>builder()
                .code(200)
                .message("Create user successfully")
                .data(data)
                .build();
    }

    @GetMapping("/profile")
    public ApiResponse<UserProfileResponse> getMyProfile(@AuthenticationPrincipal Jwt jwt){
        String userId = jwt.getSubject();
        UserProfileResponse data = userService.getMyProfile(userId);

        return ApiResponse.<UserProfileResponse>builder()
                .code(200)
                .message("Get user profile successfully")
                .data(data)
                .build();
    }
}
