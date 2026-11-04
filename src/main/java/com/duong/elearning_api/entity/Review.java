package com.duong.elearning_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "reviews",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_review_user_id_course_id",
                        columnNames = {"user_id", "course_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseEntity{
    // Học viên gửi đánh giá
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Khóa học được đánh giá
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // Số sao đánh giá (từ 1 đến 5)
    @Column(name = "rating", nullable = false)
    private Integer rating;

    // Nhận xét chi tiết của học viên
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;
}
