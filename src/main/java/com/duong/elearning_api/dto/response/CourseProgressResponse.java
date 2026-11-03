package com.duong.elearning_api.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CourseProgressResponse(
        String courseId,
        String courseTitle,
        long totalLessons,
        long completedLessons,
        BigDecimal percentCompleted,
        boolean isCourseCompleted
) {
}
