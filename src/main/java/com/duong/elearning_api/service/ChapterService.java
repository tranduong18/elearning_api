package com.duong.elearning_api.service;

import com.duong.elearning_api.dto.request.CreateChapterRequest;
import com.duong.elearning_api.dto.response.ChapterResponse;

import java.util.List;

public interface ChapterService {
    ChapterResponse createChapter(CreateChapterRequest request);

    List<ChapterResponse> getChaptersByCourseId(String courseId);
}
