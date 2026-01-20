package com.accomputers.api.application.ports.input;

import org.springframework.validation.annotation.Validated;

// DTOs
import com.accomputers.api.application.dtos.PageDTO;
import com.accomputers.api.application.dtos.ProductFiltersDTO;
import com.accomputers.api.application.dtos.createProductDTO;
import com.accomputers.api.application.dtos.response.ProductResponseDTO;
import jakarta.validation.Valid;

@Validated
public interface ProductServiceInterface {
    ProductResponseDTO createProduct(@Valid createProductDTO productDTO);
    ProductResponseDTO getProductById(Integer id);
    PageDTO<ProductResponseDTO> getAllProducts(ProductFiltersDTO queryParams);
    ProductResponseDTO updateProduct(Integer id, @Valid createProductDTO productDTO);
    void deleteProduct(Integer id);
    String getProductRecommendations(String request);
}

