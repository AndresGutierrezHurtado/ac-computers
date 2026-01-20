package com.accomputers.api.application.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.multipart.MultipartFile;

public record CreateImageDTO(
        @NotNull(message = "Product ID is required")
        @Positive(message = "Product ID must be positive")
        Integer productId,

        @NotNull(message = "Image is required")
        MultipartFile image,
        
        @NotNull(message = "IsMain is required")
        Boolean isMain) {
}
