package com.duong.elearning_api.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ChapterDetailResponse(
        String id,
        String title,
        Integer orderIndex,
        List<LessonResponse> lessons
) {
}
