package com.duong.elearning_api.dto.response;

import com.duong.elearning_api.common.CourseLevel;
import com.duong.elearning_api.common.CourseStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CourseDetailResponse(
        String id,
        String title,
        String description,
        BigDecimal price,
        CourseLevel level,
        CourseStatus status,
        String thumbnailUrl,
        Boolean isActive,
        List<ChapterResponse> chapters,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
