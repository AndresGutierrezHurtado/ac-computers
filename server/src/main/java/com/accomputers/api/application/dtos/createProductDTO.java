package com.accomputers.api.application.dtos;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record createProductDTO(
                @NotNull
                @NotBlank(message = "Name is required")
                @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
                String name,
                
                @NotNull
                @NotBlank(message = "Description is required")
                @Size(min = 3, max = 1000, message = "Description must be between 3 and 1000 characters")
                String description,
                
                @NotNull
                @Positive(message = "Price must be positive")
                Float price,
                
                @NotNull
                @NotBlank(message = "Condition is required")
                @Size(min = 3, max = 50, message = "Condition must be between 3 and 50 characters")
                String condition,
                
                Float discount,
                
                @NotNull
                @Positive(message = "Brand ID must be positive")
                Integer brandId,
                
                @NotNull
                @Positive(message = "SubCategory ID must be positive")
                Integer subCategoryId,
                
                @NotNull(message = "Images are required")
                @Size(min = 1, message = "At least one image is required")
                List<MultipartFile> images,

                List<Integer> removeImageIds,

                Integer mainImageId,

                Integer mainImageIndex,
                
                List<ProductSpecificationDTO> specifications) {

        public record ProductSpecificationDTO(
                        Integer id,
                        
                        @NotNull
                        @Positive(message = "Specification ID must be positive")
                        Integer specificationId,
                        
                        @NotNull
                        @NotBlank(message = "Value is required")
                        @Size(min = 1, max = 255, message = "Value must be between 1 and 255 characters")
                        String value,
                        
                        Integer specificationValueId) {
        }
}
