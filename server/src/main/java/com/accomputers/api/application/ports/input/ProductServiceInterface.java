package com.accomputers.api.application.ports.input;

import java.util.List;

// Domain
import com.accomputers.api.domain.entities.Product;

// DTOs
import com.accomputers.api.application.dtos.createProductDTO;

public interface ProductServiceInterface {
    Product createProduct(createProductDTO productDTO);
    Product getProductById(Integer id);
    List<Product> getAllProducts();
    Product updateProduct(Integer id, createProductDTO productDTO);
    void deleteProduct(Integer id);
    List<Product> getProductRecommendations(String request);
}

