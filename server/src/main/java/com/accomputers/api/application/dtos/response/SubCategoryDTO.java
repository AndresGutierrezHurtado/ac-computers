package com.accomputers.api.application.dtos.response;

import com.accomputers.api.domain.entities.SubCategory;

public record SubCategoryDTO(
        Integer id,
        String name,
        String slug,
        String description,
        Integer categoryId) {

    public static SubCategoryDTO fromSubCategory(SubCategory subCategory) {
        if (subCategory == null) {
            return null;
        }

        return new SubCategoryDTO(subCategory.getId(), subCategory.getName(), subCategory.getSlug().getValue(), subCategory.getDescription(), subCategory.getCategoryId());
    }
}
