package com.duong.elearning_api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateReviewRequest(
        @NotNull(message = "Điểm số đánh giá không được để trống")
        @Min(value = 1, message = "Điểm đánh giá tối thiểu là 1 sao")
        @Max(value = 5, message = "Điểm đánh giá tối đa là 5 sao")
        Integer rating,

        String comment
) {
}
