package com.duong.elearning_api.controller;

import com.duong.elearning_api.dto.request.CreateChapterRequest;
import com.duong.elearning_api.dto.response.ApiResponse;
import com.duong.elearning_api.dto.response.ChapterResponse;
import com.duong.elearning_api.service.ChapterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chapters")
@RequiredArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ChapterResponse> createChapter(@Valid @RequestBody CreateChapterRequest request) {
        ChapterResponse data = chapterService.createChapter(request);
        return ApiResponse.<ChapterResponse>builder()
                .code(201)
                .message("Create chapter successfully")
                .data(data)
                .build();
    }

    @GetMapping
    public ApiResponse<List<ChapterResponse>> getChaptersByCourseId(@RequestParam String courseId) {
        List<ChapterResponse> data = chapterService.getChaptersByCourseId(courseId);
        return ApiResponse.<List<ChapterResponse>>builder()
                .code(200)
                .message("Get list chapters successfully")
                .data(data)
                .build();
    }
}
