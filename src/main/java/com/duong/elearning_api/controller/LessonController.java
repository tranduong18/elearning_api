package com.duong.elearning_api.controller;

import com.duong.elearning_api.dto.request.CreateLessonRequest;
import com.duong.elearning_api.dto.response.ApiResponse;
import com.duong.elearning_api.dto.response.LessonResponse;
import com.duong.elearning_api.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @PostMapping
    public ApiResponse<LessonResponse> createLesson(@Valid @RequestBody CreateLessonRequest request){
        LessonResponse data = lessonService.createLesson(request);
        return ApiResponse.<LessonResponse>builder()
                .code(200)
                .message("Create lesson successfully")
                .data(data)
                .build();
    }


    @GetMapping
    public ApiResponse<List<LessonResponse>> getLessonByChapterId(@RequestParam String chapterId) {
        List<LessonResponse> data = lessonService.getLessonByChapterId(chapterId);
        return ApiResponse.<List<LessonResponse>>builder()
                .code(200)
                .message("Get list lessons successfully")
                .data(data)
                .build();
    }
}
