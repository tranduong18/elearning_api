package com.duong.elearning_api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi máy chủ không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(1000, "Unauthorized", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(1011, "Access denied", HttpStatus.FORBIDDEN),

    // Course
    COURSE_NOT_FOUND(1001, "Không tìm thấy khóa học theo yêu cầu", HttpStatus.NOT_FOUND),
    COURSE_TITLE_DUPLICATE(1002, "Tiêu đề khóa học đã tồn tại trong hệ thống", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1003, "Khóa định danh không hợp lệ", HttpStatus.BAD_REQUEST),
    COURSE_ALREADY_ENROLLED(1012, "Học viên đã đăng ký khóa học này rồi", HttpStatus.BAD_REQUEST),
    COURSE_NOT_PUBLISHED(1013, "Khóa học chưa được công khai để đăng ký", HttpStatus.BAD_REQUEST),

    // Chapter
    CHAPTER_NOT_FOUND(1004, "Không tìm thấy Chapter", HttpStatus.NOT_FOUND),

    // Lesson
    LESSON_NOT_FOUND(1014, "Không tìm thấy bài học theo yêu cầu", HttpStatus.NOT_FOUND),

    // User
    USER_EXISTED(1005, "Người dùng đã tồn tại trong hệ thống", HttpStatus.CONFLICT),
    USER_NOT_FOUND(1006, "Người dùng không tồn tại trên hệ thống", HttpStatus.NOT_FOUND),

    // Review
    NOT_ENROLLED(1014, "Bạn chưa đăng ký khóa học này nên không thể đánh giá", HttpStatus.FORBIDDEN),
    REVIEW_ALREADY_EXISTS(1015, "Bạn đã đánh giá khóa học này rồi", HttpStatus.BAD_REQUEST),
    REVIEW_NOT_FOUND(1016, "Không tìm thấy đánh giá yêu cầu", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_REVIEW_ACCESS(1017, "Bạn không có quyền chỉnh sửa hoặc xóa đánh giá này", HttpStatus.FORBIDDEN),

    // File
    FILE_EMPTY(4001, "Tệp tin tải lên không được để trống", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE(4002, "Kích thước tệp vượt quá dung lượng cho phép (tối đa 10MB)", HttpStatus.BAD_REQUEST),
    INVALID_IMAGE_FORMAT(4003, "Định dạng ảnh không hợp lệ (chỉ hỗ trợ JPG, JPEG, PNG, WEBP)", HttpStatus.BAD_REQUEST),
    UPLOAD_FAILED(4004, "Tải tệp tin lên Cloudinary thất bại", HttpStatus.INTERNAL_SERVER_ERROR),

    // Token
    REFRESH_TOKEN_NOT_FOUND(1007, "Refresh token không tồn tại trên hệ thống", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_EXPIRED(1008, "Refresh token đã hết hạn hoặc bị thu hồi", HttpStatus.UNAUTHORIZED),

    // Password
    PASSWORD_NOT_MATCH(1009, "Mật khẩu cũ không chính xác", HttpStatus.BAD_REQUEST),
    SAME_OLD_PASSWORD(1010, "Mật khẩu mới không được trùng với mật khẩu cũ", HttpStatus.BAD_REQUEST),
    ;

    private final int code;
    private final String message;
    private final HttpStatus status;
}
