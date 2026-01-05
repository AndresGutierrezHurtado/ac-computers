package com.accomputers.api.application.dtos.response;

import com.accomputers.api.domain.entities.SpecificationValue;

public record SpecificationValueResponseDTO(
        Integer id,
        Integer specificationId,
        String value,
        Integer order) {

    public static SpecificationValueResponseDTO fromSpecificationValue(SpecificationValue specificationValue) {
        if (specificationValue == null) {
            return null;
        }

        return new SpecificationValueResponseDTO(
                specificationValue.getId(),
                specificationValue.getSpecificationId(),
                specificationValue.getValue(),
                specificationValue.getOrder());
    }
}

