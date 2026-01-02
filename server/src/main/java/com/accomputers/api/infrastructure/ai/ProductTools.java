package com.accomputers.api.infrastructure.ai;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.accomputers.api.application.dtos.PageDTO;
import com.accomputers.api.application.dtos.ProductCriteria;
import com.accomputers.api.application.ports.output.repositories.ProductRepositoryInterface;
import com.accomputers.api.domain.entities.Product;
import java.util.List;

@Component
public class ProductTools {
    private final ProductRepositoryInterface productRepository;

    @Autowired
    public ProductTools(ProductRepositoryInterface productRepository) {
        this.productRepository = productRepository;
    }

    @Tool(description = "Lista todos los productos disponibles")
    public List<Product> getAllProducts() {
        PageDTO<Product> pageDTO = productRepository.findAll(new ProductCriteria(1, 20, null, null, null, null, null, null, null, null, null));
        return pageDTO.data();
    }
}
