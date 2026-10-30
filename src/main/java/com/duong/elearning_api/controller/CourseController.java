package com.duong.elearning_api.controller;

import com.duong.elearning_api.dto.request.CreateCourseRequest;
import com.duong.elearning_api.dto.response.ApiResponse;
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

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
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
    public ResponseEntity<CourseDetailResponse> updateCourse(@PathVariable String id, @RequestBody Course course) {
        CourseDetailResponse updated = courseService.updateCourse(id, course);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
