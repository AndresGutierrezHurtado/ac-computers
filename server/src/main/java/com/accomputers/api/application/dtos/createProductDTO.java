package com.accomputers.api.application.dtos;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public record createProductDTO(
                String name,
                String description,
                Float price,
                String condition,
                Float discount,
                Integer brandId,
                Integer subCategoryId,
                MultipartFile image,
                List<ProductSpecificationDTO> specifications) {

        public record ProductSpecificationDTO(
                        Integer id,
                        Integer specificationId,
                        String value,
                        Integer specificationValueId) {
        }
}
