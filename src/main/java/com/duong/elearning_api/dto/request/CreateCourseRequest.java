package com.duong.elearning_api.dto.request;

import com.duong.elearning_api.common.CourseLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

public record CreateCourseRequest(
        @Schema(description = "Tiêu đề của khóa học", example = "Lập trình Spring Boot từ số 0")
        @NotBlank(message = "Tiêu đề khóa học không được để trống")
        @Size(min = 5, max = 200, message = "Tiêu đề khóa học phải từ 5 đến 200 ký tự")
        String title,

        @Schema(description = "Mô tả chi tiết nội dung khóa học", example = "Khóa học đi từ bản chất Spring Framework đến kiến trúc nâng cao")
        @Size(max = 2000, message = "Mô tả khóa học không được dài quá 2000 ký tự")
        String description,

        @Schema(description = "Giá bán của khóa học (VNĐ)", example = "1299000.00")
        @NotNull(message = "Giá khóa học không được để trống")
        @DecimalMin(value = "0.0", message = "Giá khóa học không được là số âm")
        BigDecimal price,

        @Schema(description = "Trình độ của khóa học", example = "BEGINNER")
        @NotNull(message = "Cấp độ khóa học không được để trống")
        CourseLevel level,

        @URL(message = "Đường dẫn ảnh đại diện phải là một URL hợp lệ")
        String thumbnailUrl
) {
}
