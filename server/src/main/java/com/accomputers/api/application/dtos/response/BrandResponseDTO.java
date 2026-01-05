package com.accomputers.api.application.dtos.response;

import com.accomputers.api.domain.entities.Brand;

public record BrandResponseDTO(
        Integer id,
        String name) {

    public static BrandResponseDTO fromBrand(Brand brand) {
        if (brand == null) {
            return null;
        }

        return new BrandResponseDTO(
                brand.getId(),
                brand.getName());
    }
}

