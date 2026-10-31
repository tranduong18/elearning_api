package com.duong.elearning_api.dto.response;

import lombok.Builder;

@Builder
public record LessonResponse(
        String id,
        String title,
        String videoUrl,
        Integer durationSeconds,
        Boolean isFreePreview,
        Integer orderIndex
) {
}
