package com.duong.elearning_api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateProgressRequest(
        @NotBlank(message = "ID bài học không được để trống")
        String lessonId,

        @NotNull(message = "Số giây đã xem không được để trống")
        @Min(value = 0, message = "Số giây xem không thể âm")
        Integer watchedSeconds,

        @NotNull(message = "Trạng thái hoàn thành không được để trống")
        Boolean isCompleted
) {
}
