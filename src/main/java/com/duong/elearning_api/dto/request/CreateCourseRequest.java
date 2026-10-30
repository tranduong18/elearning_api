package com.duong.elearning_api.dto.request;

import com.duong.elearning_api.common.CourseLevel;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateCourseRequest(
        @NotBlank(message = "Tiêu đề khóa học không được để trống")
        @Size(min = 5, max = 200, message = "Tiêu đề khóa học phải từ 5 đến 200 ký tự")
        String title,

        @Size(max = 2000, message = "Mô tả khóa học không được dài quá 2000 ký tự")
        String description,

        @NotNull(message = "Giá khóa học không được để trống")
        @DecimalMin(value = "0.0", inclusive = true, message = "Giá khóa học không được là số âm")
        BigDecimal price,

        @NotNull(message = "Cấp độ khóa học không được để trống")
        CourseLevel level
) {
}
