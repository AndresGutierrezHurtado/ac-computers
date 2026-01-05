package com.accomputers.api.application.dtos.response;

import com.accomputers.api.domain.entities.Specification;

public record SpecificationResponseDTO(
        Integer id,
        String name,
        String slug,
        String type,
        String unit,
        Boolean isFilterable,
        Boolean isMandatory,
        Integer subCategoryId) {

    public static SpecificationResponseDTO fromSpecification(Specification specification) {
        if (specification == null) {
            return null;
        }

        return new SpecificationResponseDTO(
                specification.getId(),
                specification.getName(),
                specification.getSlug() != null ? specification.getSlug().getValue() : null,
                specification.getType(),
                specification.getUnit(),
                specification.getIsFilterable(),
                specification.getIsMandatory(),
                specification.getSubCategoryId());
    }
}

