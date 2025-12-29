package com.accomputers.api.application.ports.input;

import com.accomputers.api.application.dtos.createProductDTO;
import com.accomputers.api.domain.entities.Product;
import java.util.List;

public interface ProductUseCase {
    Product createProduct(createProductDTO productDTO);
    Product getProductById(Integer id);
    List<Product> getAllProducts();
    Product updateProduct(Integer id, createProductDTO productDTO);
    void deleteProduct(Integer id);
    List<Product> getProductRecommendations(String request);
}

