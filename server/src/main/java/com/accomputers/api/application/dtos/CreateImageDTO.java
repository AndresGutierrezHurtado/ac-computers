package com.accomputers.api.application.dtos;

import org.springframework.web.multipart.MultipartFile;

public record CreateImageDTO(
        Integer productId,
        MultipartFile image,
        Boolean isMain) {
}
