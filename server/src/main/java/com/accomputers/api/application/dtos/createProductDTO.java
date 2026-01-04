package com.accomputers.api.application.dtos;

import java.util.List;

public record createProductDTO(
        String name,
        String description,
        double price,
        String condition,
        double discount,
        int categoryId,
        int brandId,
        int subCategoryId,
        List<ImageDTO> images,
        List<ProductSpecificationDTO> specifications) {

    public record ImageDTO(String url, Boolean isMain) {
    }

    public record ProductSpecificationDTO(
            Integer specificationId,
            String value,
            Integer specificationValueId) {
    }
}
