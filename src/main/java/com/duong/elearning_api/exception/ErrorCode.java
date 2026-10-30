package com.duong.elearning_api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi máy chủ không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    COURSE_NOT_FOUND(1001, "Không tìm thấy khóa học theo yêu cầu", HttpStatus.NOT_FOUND),
    COURSE_TITLE_DUPLICATE(1002, "Tiêu đề khóa học đã tồn tại trong hệ thống", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1003, "Khóa định danh không hợp lệ", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
