package com.accomputers.api.application.dtos.response;

import com.accomputers.api.domain.entities.Category;

public record CategoryResponseDTO(
        Integer id,
        String name,
        String slug,
        String description) {

    public static CategoryResponseDTO fromCategory(Category category) {
        if (category == null) {
            return null;
        }

        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getSlug() != null ? category.getSlug().getValue() : null,
                category.getDescription());
    }
}
