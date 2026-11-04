package com.duong.elearning_api.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReviewResponse(
        String id,
        String userId,
        String userEmail,
        Integer rating,
        String comment,
        LocalDateTime createdAt
) {
}
