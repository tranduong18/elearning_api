package com.duong.elearning_api.controller;

import com.duong.elearning_api.dto.request.CreateCourseRequest;
import com.duong.elearning_api.dto.request.UpdateCourseRequest;
import com.duong.elearning_api.dto.response.ApiResponse;
import com.duong.elearning_api.dto.response.CourseCurriculumResponse;
import com.duong.elearning_api.dto.response.CourseDetailResponse;
import com.duong.elearning_api.entity.Course;
import com.duong.elearning_api.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    public ApiResponse<CourseDetailResponse> createCourse(@RequestBody @Valid CreateCourseRequest request) {
        CourseDetailResponse data = courseService.createCourse(request);
        return ApiResponse.<CourseDetailResponse>builder()
                .code(200)
                .message("Create course successfully")
                .data(data)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CourseDetailResponse> getCourseById(@PathVariable String id) {
        CourseDetailResponse data = courseService.getCourseById(id);
        return ApiResponse.<CourseDetailResponse>builder()
                .code(200)
                .message("Get course successfully")
                .data(data)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CourseDetailResponse> updateCourse(@PathVariable String id, @RequestBody @Valid UpdateCourseRequest request) {
        CourseDetailResponse data = courseService.updateCourse(id, request);
        return ApiResponse.<CourseDetailResponse>builder()
                .code(200)
                .message("Update course successfully")
                .data(data)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCourse(@PathVariable String id) {
        courseService.deleteCourse(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete course successfully")
                .build();
    }

    @GetMapping("/with-chapters")
    public ApiResponse<List<CourseDetailResponse>> getAllCoursesWithChapters() {
        List<CourseDetailResponse> data = courseService.getAllCoursesWithChapters();
        return ApiResponse.<List<CourseDetailResponse>>builder()
                .code(200)
                .message("Get list courses successfully")
                .data(data)
                .build();
    }

    @GetMapping("/{id}/curriculum")
    public ApiResponse<CourseCurriculumResponse> getCourseCurriculum(@PathVariable String id) {
        CourseCurriculumResponse data = courseService.getCourseCurriculum(id);
        return ApiResponse.<CourseCurriculumResponse>builder()
                .code(200)
                .message("Get full course successfully")
                .data(data)
                .build();
    }
}
