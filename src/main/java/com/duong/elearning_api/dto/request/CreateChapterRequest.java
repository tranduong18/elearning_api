package com.duong.elearning_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateChapterRequest(
        @NotBlank(message = "Tiêu đề chương không được để trống")
        @Size(max = 200, message = "Tiêu đề chương không được vượt quá 200 ký tự")
        String title,

        @NotBlank(message = "ID khóa học không được để trống")
        String courseId
) {
}
