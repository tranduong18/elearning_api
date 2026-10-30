package com.duong.elearning_api.dto.response;

import com.duong.elearning_api.common.CourseLevel;
import com.duong.elearning_api.common.CourseStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record CourseDetailResponse(
        String id,
        String title,
        String description,
        BigDecimal price,
        CourseLevel level,
        CourseStatus status,
        String thumbnailUrl,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
