package com.accomputers.api.application.dtos.response;

import com.accomputers.api.domain.entities.ProductSpecification;

public record ProductSpecificationResponseDTO(
        Integer id,
        String value,
        SpecificationResponseDTO specification) {

    public static ProductSpecificationResponseDTO fromProductSpecification(ProductSpecification productSpecification) {
        if (productSpecification == null) {
            return null;
        }

        SpecificationResponseDTO specificationDTO = null;
        if (productSpecification.getSpecification() != null) {
            specificationDTO = SpecificationResponseDTO.fromSpecification(productSpecification.getSpecification());
        }

        String value = productSpecification.getValue();

        if (value == null && productSpecification.getSpecificationValue() != null) {
            value = productSpecification.getSpecificationValue().getValue();
        }

        return new ProductSpecificationResponseDTO(
                productSpecification.getId(),
                value,
                specificationDTO);
    }
}

