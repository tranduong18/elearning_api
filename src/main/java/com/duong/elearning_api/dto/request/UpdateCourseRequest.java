package com.duong.elearning_api.dto.request;

import com.duong.elearning_api.common.CourseLevel;
import com.duong.elearning_api.common.CourseStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateCourseRequest(
    @Size(min = 5, max = 200, message = "Tiêu đề khóa học phải từ 5 đến 200 ký tự")
    String title,

    @Size(max = 2000, message = "Mô tả khóa học không được vượt quá 2000 ký tự")
    String description,

    @DecimalMin(value = "0.0", message = "Giá khóa học không được là số âm")
    BigDecimal price,

    CourseLevel level,
    CourseStatus status
) {
}
