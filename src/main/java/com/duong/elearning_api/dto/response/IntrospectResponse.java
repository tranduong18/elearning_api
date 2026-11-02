package com.duong.elearning_api.dto.response;

import lombok.Builder;

import java.util.Set;

@Builder
public record IntrospectResponse(
        boolean isValid,
        Set<String> roles
) {
}
