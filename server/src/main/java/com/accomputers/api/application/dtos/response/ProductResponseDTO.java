package com.accomputers.api.application.dtos.response;

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
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        BrandResponseDTO brand,
        SubCategoryDTO subCategory,
        List<ImageResponseDTO> images,
        List<ProductSpecificationResponseDTO> productSpecifications) {

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

        List<ProductSpecificationResponseDTO> productSpecificationsDTO = null;
        if (product.getProductSpecifications() != null) {
            productSpecificationsDTO = product.getProductSpecifications().stream()
                    .map(ProductSpecificationResponseDTO::fromProductSpecification)
                    .collect(Collectors.toList());
        }

        BrandResponseDTO brandDTO = null;
        if (product.getBrand() != null) {
            brandDTO = BrandResponseDTO.fromBrand(product.getBrand());
        }

        SubCategoryDTO subCategoryDTO = null;
        if (product.getSubCategory() != null) {
            subCategoryDTO = SubCategoryDTO.fromSubCategory(product.getSubCategory());
        }

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice() != null ? product.getPrice().getValue() : null,
                product.getCondition() != null ? product.getCondition().getValue() : null,
                product.getDiscount() != null ? product.getDiscount().getValue() : null,
                product.getCreatedAt(),
                product.getUpdatedAt(),
                brandDTO,
                subCategoryDTO,
                imagesDTO,
                productSpecificationsDTO);
    }
}

