package com.duong.elearning_api.dto.response;

import lombok.Builder;

@Builder
public record ChapterResponse(
        String id,
        String title,
        Integer orderIndex
) {
}
