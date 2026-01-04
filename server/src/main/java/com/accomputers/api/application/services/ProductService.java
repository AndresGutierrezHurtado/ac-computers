package com.accomputers.api.application.services;

// Domain
import com.accomputers.api.domain.entities.Image;
import com.accomputers.api.domain.entities.Product;
import com.accomputers.api.domain.entities.ProductSpecification;
import com.accomputers.api.domain.exceptions.EntityNotFoundException;
import com.accomputers.api.domain.valueobjects.Condition;
import com.accomputers.api.domain.valueobjects.Discount;
import com.accomputers.api.domain.valueobjects.Price;
import com.accomputers.api.domain.valueobjects.Url;

// Ports
import com.accomputers.api.application.ports.input.ProductServiceInterface;
import com.accomputers.api.application.ports.output.ProductRecommendationInterface;
import com.accomputers.api.application.ports.output.repositories.ImageRepositoryInterface;
import com.accomputers.api.application.ports.output.repositories.ProductRepositoryInterface;
import com.accomputers.api.application.ports.output.repositories.ProductSpecificationRepositoryInterface;
import com.accomputers.api.application.dtos.PageDTO;
import com.accomputers.api.application.dtos.ProductCriteria;
import com.accomputers.api.application.dtos.ProductFiltersDTO;
// DTOs
import com.accomputers.api.application.dtos.createProductDTO;
import com.accomputers.api.application.dtos.response.ProductResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService implements ProductServiceInterface {
    private final ProductRepositoryInterface productRepository;
    private final ProductRecommendationInterface productRecommendationInterface;
    private final ImageRepositoryInterface imageRepository;
    private final ProductSpecificationRepositoryInterface productSpecificationRepository;

    @Autowired
    public ProductService(
            ProductRepositoryInterface productRepository,
            ProductRecommendationInterface productRecommendationInterface,
            ImageRepositoryInterface imageRepository,
            ProductSpecificationRepositoryInterface productSpecificationRepository) {
        this.productRepository = productRepository;
        this.productRecommendationInterface = productRecommendationInterface;
        this.imageRepository = imageRepository;
        this.productSpecificationRepository = productSpecificationRepository;
    }

    @Override
    public ProductResponseDTO createProduct(createProductDTO productDTO) {
        LocalDateTime now = LocalDateTime.now();

        String conditionValue = StringUtils.hasText(productDTO.condition()) 
            ? productDTO.condition() 
            : "new";

        Product product = new Product(
                null,
                productDTO.name(),
                productDTO.description(),
                new Price((float) productDTO.price()),
                new Condition(conditionValue),
                new Discount((float) productDTO.discount()),
                productDTO.brandId(),
                productDTO.subCategoryId(),
                null,
                now,
                now
        );

        Product savedProduct = productRepository.save(product);
        return ProductResponseDTO.fromProduct(savedProduct);
    }

    @Override
    public ProductResponseDTO getProductById(Integer id) {
        Product product = productRepository.findById(id);

        if (product == null) {
            throw new EntityNotFoundException("Product", id);
        }

        return ProductResponseDTO.fromProduct(product);
    }

    @Override
    public PageDTO<ProductResponseDTO> getAllProducts(ProductFiltersDTO queryParams) {
        ProductCriteria productCriteria = queryParams.toProductCriteria();
        PageDTO<Product> pageDTO = productRepository.findAll(productCriteria);
        List<ProductResponseDTO> productResponseDTOs = pageDTO.data().stream()
                .map(ProductResponseDTO::fromProduct)
                .collect(Collectors.toList());
        return new PageDTO<>(productResponseDTOs, pageDTO.total());
    }

    @Override
    public ProductResponseDTO updateProduct(Integer id, createProductDTO productDTO) {
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
        if (StringUtils.hasText(productDTO.condition())) {
            product.setCondition(new Condition(productDTO.condition()));
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

        // Preserve brand if it exists
        updatedProduct.setBrand(product.getBrand());

        Product savedProduct = productRepository.save(updatedProduct);
        return ProductResponseDTO.fromProduct(savedProduct);
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
