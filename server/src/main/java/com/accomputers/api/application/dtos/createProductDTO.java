package com.accomputers.api.application.dtos;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public record createProductDTO(
                String name,
                String description,
                double price,
                String condition,
                double discount,
                int brandId,
                int subCategoryId,
                List<ImageDTO> images,
                List<ProductSpecificationDTO> specifications) {

        public record ImageDTO(Integer id, MultipartFile file, Boolean isMain) {
        }

        public record ProductSpecificationDTO(
                        Integer id,
                        Integer specificationId,
                        String value,
                        Integer specificationValueId) {
        }
}
