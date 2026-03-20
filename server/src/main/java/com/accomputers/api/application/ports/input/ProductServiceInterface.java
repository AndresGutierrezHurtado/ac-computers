package com.accomputers.api.application.ports.input;


// DTOs
import com.accomputers.api.application.dtos.*;
import com.accomputers.api.application.dtos.response.ProductAiOverviewResponse;
import com.accomputers.api.application.dtos.response.ProductResponseDTO;

public interface ProductServiceInterface {
    ProductResponseDTO createProduct(createProductDTO productDTO);
    ProductResponseDTO getProductById(Integer id);

    ProductAiOverviewResponse getProductAiOverview(Integer id);
    PageDTO<ProductResponseDTO> getAllProducts(ProductFiltersDTO queryParams);
    ProductResponseDTO updateProduct(Integer id, createProductDTO productDTO);
    void deleteProduct(Integer id);
    SalesChatResponse chat(SalesChatRequest request);
}

