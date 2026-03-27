package com.accomputers.api.application.ports.input;


// DTOs
import com.accomputers.api.application.dtos.*;
import com.accomputers.api.application.dtos.response.ProductAiOverviewResponse;
import com.accomputers.api.application.dtos.response.ProductResponseDTO;
import reactor.core.publisher.Flux;

public interface ProductServiceInterface {
    ProductResponseDTO createProduct(createProductDTO productDTO);
    ProductResponseDTO getProductById(Integer id);

    Flux<ProductAiOverviewResponse> getProductAiOverview(Integer id);
    PageDTO<ProductResponseDTO> getAllProducts(ProductFiltersDTO queryParams);
    ProductResponseDTO updateProduct(Integer id, createProductDTO productDTO);
    void deleteProduct(Integer id);
    SalesChatResponse chat(SalesChatRequest request);
}
