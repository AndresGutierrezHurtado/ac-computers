package com.accomputers.api.application.services;

// Domain
import com.accomputers.api.domain.entities.Product;
import com.accomputers.api.domain.exceptions.EntityNotFoundException;
import com.accomputers.api.domain.valueobjects.Condition;
import com.accomputers.api.domain.valueobjects.Discount;
import com.accomputers.api.domain.valueobjects.Price;

// Ports
import com.accomputers.api.application.ports.input.ProductServiceInterface;
import com.accomputers.api.application.ports.output.ProductRecommendationInterface;
import com.accomputers.api.application.ports.output.repositories.ProductRepositoryInterface;

// DTOs
import com.accomputers.api.application.dtos.createProductDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService implements ProductServiceInterface {
    private final ProductRepositoryInterface productRepository;
    private final ProductRecommendationInterface productRecommendationInterface;

    @Autowired
    public ProductService(
            ProductRepositoryInterface productRepository,
            ProductRecommendationInterface productRecommendationInterface) {
        this.productRepository = productRepository;
        this.productRecommendationInterface = productRecommendationInterface;
    }

    @Override
    public Product createProduct(createProductDTO productDTO) {
        LocalDateTime now = LocalDateTime.now();

        Product product = new Product(
                null,
                productDTO.name(),
                productDTO.description(),
                new Price((float) productDTO.price()),
                new Condition("new"),
                new Discount((float) productDTO.discount()),
                productDTO.brandId(),
                productDTO.subCategoryId(),
                null,
                now,
                now
        );

        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Integer id) {
        Product product = productRepository.findById(id);

        if (product == null) {
            throw new EntityNotFoundException("Product", id);
        }

        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product updateProduct(Integer id, createProductDTO productDTO) {
        Product product = productRepository.findById(id);

        if (product == null) {
            throw new EntityNotFoundException("Product", id);
        }

        // Update fields
        if (productDTO.name() != null) {
            product.setName(productDTO.name());
        }
        if (productDTO.description() != null) {
            product.setDescription(productDTO.description());
        }
        if (productDTO.price() >= 0) {
            product.setPrice(new Price((float) productDTO.price()));
        }
        if (productDTO.discount() >= 0) {
            product.setDiscount(new Discount((float) productDTO.discount()));
        }
        product.setBrandId(productDTO.brandId());
        product.setSubCategoryId(productDTO.subCategoryId());

        // Create new Product instance with updated timestamp
        Product updatedProduct = new Product(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCondition(),
                product.getDiscount(),
                product.getBrandId(),
                product.getSubCategoryId(),
                product.getDeletedAt(),
                product.getCreatedAt(),
                LocalDateTime.now()
        );

        // Preserve brand and images if they exist
        updatedProduct.setBrand(product.getBrand());
        updatedProduct.setImages(product.getImages());

        return productRepository.save(updatedProduct);
    }

    @Override
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id);

        if (product == null) {
            throw new EntityNotFoundException("Product", id);
        }

        productRepository.delete(id);
    }

    @Override
    public String getProductRecommendations(String request) {
        return productRecommendationInterface.getRecommendations(request);
    }
}
