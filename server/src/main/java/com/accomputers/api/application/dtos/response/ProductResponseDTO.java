package com.accomputers.api.application.dtos.response;

import com.accomputers.api.domain.entities.Brand;
import com.accomputers.api.domain.entities.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ProductResponseDTO(
        Integer id,
        String name,
        String description,
        Float price,
        String condition,
        Float discount,
        Integer brandId,
        Integer subCategoryId,
        LocalDateTime deletedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Brand brand,
        List<ImageResponseDTO> images) {

    public static ProductResponseDTO fromProduct(Product product) {
        if (product == null) {
            return null;
        }

        List<ImageResponseDTO> imagesDTO = null;
        if (product.getImages() != null) {
            imagesDTO = product.getImages().stream()
                    .map(ImageResponseDTO::fromImage)
                    .collect(Collectors.toList());
        }

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice() != null ? product.getPrice().getValue() : null,
                product.getCondition() != null ? product.getCondition().getValue() : null,
                product.getDiscount() != null ? product.getDiscount().getValue() : null,
                product.getBrandId(),
                product.getSubCategoryId(),
                product.getDeletedAt(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getBrand(),
                imagesDTO);
    }
}

