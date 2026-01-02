package com.accomputers.api.application.ports.input;

import java.util.List;

// DTOs
import com.accomputers.api.application.dtos.createProductDTO;
import com.accomputers.api.application.dtos.response.ProductResponseDTO;

public interface ProductServiceInterface {
    ProductResponseDTO createProduct(createProductDTO productDTO);
    ProductResponseDTO getProductById(Integer id);
    List<ProductResponseDTO> getAllProducts();
    ProductResponseDTO updateProduct(Integer id, createProductDTO productDTO);
    void deleteProduct(Integer id);
    String getProductRecommendations(String request);
}

