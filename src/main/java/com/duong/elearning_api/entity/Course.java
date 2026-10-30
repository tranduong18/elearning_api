package com.duong.elearning_api.entity;

import com.duong.elearning_api.common.CourseLevel;
import com.duong.elearning_api.common.CourseStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CourseLevel level;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CourseStatus status;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
