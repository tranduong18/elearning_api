package com.duong.elearning_api.common;

public enum EnrollmentStatus {
    ACTIVE,     // Đang trong thời gian học hợp lệ
    COMPLETED,  // Đã hoàn thành 100% thời lượng các bài học
    CANCELLED   // Đã bị hủy tư cách (hoàn tiền hoặc vi phạm điều khoản)
}
