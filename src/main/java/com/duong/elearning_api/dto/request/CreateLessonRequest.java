package com.duong.elearning_api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateLessonRequest(
        @NotBlank(message = "Tiêu đề bài học không được để trống")
        @Size(max = 200, message = "Tiêu đề bài học không được vượt quá 200 ký tự")
        String title,

        @NotBlank(message = "ID Chapter không được để trống")
        String chapterId,

        String videoUrl,

        @Min(value = 0, message = "Thời lượng video không được là số âm")
        Integer durationSeconds,

        Boolean isFreePreview
) {
}
