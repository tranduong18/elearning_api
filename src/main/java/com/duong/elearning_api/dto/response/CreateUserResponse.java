package com.duong.elearning_api.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record CreateUserResponse(
    String id,
    String email,
    String fullName,
    String phoneNumber,
    Boolean isActive,
    Set<String> roles,
    LocalDateTime createdAt
) {
}
