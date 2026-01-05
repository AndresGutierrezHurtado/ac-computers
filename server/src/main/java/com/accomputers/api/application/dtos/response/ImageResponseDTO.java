package com.accomputers.api.application.dtos.response;

import com.accomputers.api.domain.entities.Image;

public record ImageResponseDTO(
        Integer id,
        String url,
        Boolean isMain) {

    public static ImageResponseDTO fromImage(Image image) {
        if (image == null) {
            return null;
        }

        return new ImageResponseDTO(
                image.getId(),
                image.getUrl() != null ? image.getUrl().getValue() : null,
                image.getIsMain());
    }
}

