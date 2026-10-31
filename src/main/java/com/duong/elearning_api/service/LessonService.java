package com.duong.elearning_api.service;

import com.duong.elearning_api.dto.request.CreateLessonRequest;
import com.duong.elearning_api.dto.response.LessonResponse;

import java.util.List;

public interface LessonService {
    List<LessonResponse> getLessonByChapterId(String chapterId);

    LessonResponse createLesson(CreateLessonRequest request);
}
