package com.accomputers.api.application.ports.input;

// DTOs
import com.accomputers.api.application.dtos.PageDTO;
import com.accomputers.api.application.dtos.ProductQueryParamsDTO;
import com.accomputers.api.application.dtos.createProductDTO;
import com.accomputers.api.application.dtos.response.ProductResponseDTO;

public interface ProductServiceInterface {
    ProductResponseDTO createProduct(createProductDTO productDTO);
    ProductResponseDTO getProductById(Integer id);
    PageDTO<ProductResponseDTO> getAllProducts(ProductQueryParamsDTO queryParams);
    ProductResponseDTO updateProduct(Integer id, createProductDTO productDTO);
    void deleteProduct(Integer id);
    String getProductRecommendations(String request);
}

