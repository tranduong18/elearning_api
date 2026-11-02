package com.duong.elearning_api.dto.response;

import lombok.Builder;

import java.util.Set;

@Builder
public record UserProfileResponse(
        String id,
        String email,
        String fullName,
        String phoneNumber,
        String avatarUrl,
        Boolean isActive,
        Set<String> roles
) {
}
