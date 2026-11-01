package com.duong.elearning_api.controller;

import com.duong.elearning_api.dto.request.CreateUserRequest;
import com.duong.elearning_api.dto.response.ApiResponse;
import com.duong.elearning_api.dto.response.CreateUserResponse;
import com.duong.elearning_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
