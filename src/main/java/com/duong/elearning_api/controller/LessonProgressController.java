package com.duong.elearning_api.controller;

import com.duong.elearning_api.dto.request.UpdateProgressRequest;
import com.duong.elearning_api.dto.response.ApiResponse;
import com.duong.elearning_api.dto.response.CourseProgressResponse;
import com.duong.elearning_api.service.LessonProgressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LessonProgressController {
    private final LessonProgressService lessonProgressService;

    // 1. Cập nhật tiến độ xem video bài học (Upsert)
    @PutMapping("/progress")
    public ApiResponse<Void> updateProgress(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateProgressRequest request
    ){
        String userId = jwt.getSubject();
        lessonProgressService.upsertProgress(userId, request);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Update progress successfully")
                .build();
    }

    // 2. Lấy thông tin thanh tiến độ của toàn bộ khóa học
    @GetMapping("/courses/{courseId}/progress")
    public ApiResponse<CourseProgressResponse> getCourseProgress(
            @PathVariable String courseId,
            @AuthenticationPrincipal Jwt jwt
    ){
        String userId = jwt.getSubject();
        CourseProgressResponse data = lessonProgressService.getCourseProgress(userId, courseId);
        return ApiResponse.<CourseProgressResponse>builder()
                .code(200)
                .message("Get course progress successfully")
                .data(data)
                .build();
    }

}
