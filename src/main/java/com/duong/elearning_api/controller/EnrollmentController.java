package com.duong.elearning_api.controller;

import com.duong.elearning_api.dto.request.EnrollCourseRequest;
import com.duong.elearning_api.dto.response.ApiResponse;
import com.duong.elearning_api.dto.response.EnrollmentResponse;
import com.duong.elearning_api.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EnrollmentResponse> enrollCourse(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody EnrollCourseRequest request
    ){
        String userId = jwt.getSubject();
        EnrollmentResponse data = enrollmentService.enrollCourse(userId, request);

        return ApiResponse.<EnrollmentResponse>builder()
                .code(200)
                .message("Enroll course successfully")
                .data(data)
                .build();
    }

    @GetMapping("/my-enrollments")
    public ApiResponse<List<EnrollmentResponse>> getMyEnrollments(@AuthenticationPrincipal Jwt jwt){
        String userId = jwt.getSubject();
        List<EnrollmentResponse> data = enrollmentService.getMyEnrollments(userId);

        return ApiResponse.<List<EnrollmentResponse>>builder()
                .code(200)
                .message("Get my enrollments successfully")
                .data(data)
                .build();
    }
}
