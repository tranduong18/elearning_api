package com.duong.elearning_api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EnrollCourseRequest(
        @NotBlank(message = "ID khóa học không được để trống")
        String courseId
) {
}
