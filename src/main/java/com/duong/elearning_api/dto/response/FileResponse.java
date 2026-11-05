package com.duong.elearning_api.dto.response;

import lombok.Builder;

@Builder
public record FileResponse(
        String publicId,
        String secureUrl,
        String format
) {
}
