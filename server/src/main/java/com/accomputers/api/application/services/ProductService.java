package com.accomputers.api.application.services;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

// Domain
import com.accomputers.api.domain.entities.*;
import com.accomputers.api.domain.exceptions.EntityNotFoundException;
import com.accomputers.api.domain.valueobjects.Condition;
import com.accomputers.api.domain.valueobjects.Discount;
import com.accomputers.api.domain.valueobjects.Price;
import com.accomputers.api.domain.valueobjects.Url;

// Ports
import com.accomputers.api.application.ports.input.ProductServiceInterface;
import com.accomputers.api.application.ports.output.FileManagerInterface;
import com.accomputers.api.application.ports.output.LoggerPort;
import com.accomputers.api.application.ports.output.ProductRecommendationInterface;
import com.accomputers.api.application.ports.output.repositories.*;

// DTOs
import com.accomputers.api.application.dtos.PageDTO;
import com.accomputers.api.application.dtos.ProductCriteria;
import com.accomputers.api.application.dtos.ProductFiltersDTO;
import com.accomputers.api.application.dtos.createProductDTO;
import com.accomputers.api.application.dtos.createProductDTO.ProductSpecificationDTO;
import com.accomputers.api.application.dtos.response.ProductResponseDTO;

@Service
public class ProductService implements ProductServiceInterface {
    private final ProductRepositoryInterface productRepository;
    private final ProductRecommendationInterface productRecommendationInterface;
    private final FileManagerInterface fileManagerInterface;
    private final ImageRepositoryInterface imageRepository;
    private final ProductSpecificationRepositoryInterface productSpecificationRepository;
    private final BrandRepositoryInterface brandRepository;
    private final SubCategoryRepositoryInterface subCategoryRepository;
    private final SpecificationRepositoryInterface specificationRepository;
    private final SpecificationValueRepositoryInterface specificationValueRepository;
    private final LoggerPort loggerPort;

    @Autowired
    public ProductService(
            ProductRepositoryInterface productRepository,
            ProductRecommendationInterface productRecommendationInterface,
            ImageRepositoryInterface imageRepository,
            ProductSpecificationRepositoryInterface productSpecificationRepository,
            BrandRepositoryInterface brandRepository,
            SubCategoryRepositoryInterface subCategoryRepository,
            SpecificationRepositoryInterface specificationRepository,
            SpecificationValueRepositoryInterface specificationValueRepository,
            FileManagerInterface fileManagerInterface,
            LoggerPort loggerPort) {
        this.productRepository = productRepository;
        this.productRecommendationInterface = productRecommendationInterface;
        this.imageRepository = imageRepository;
        this.productSpecificationRepository = productSpecificationRepository;
        this.brandRepository = brandRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.specificationRepository = specificationRepository;
        this.specificationValueRepository = specificationValueRepository;
        this.fileManagerInterface = fileManagerInterface;
        this.loggerPort = loggerPort;
    }

    @Override
    @Transactional
    public ProductResponseDTO createProduct(createProductDTO productDTO) {
        LocalDateTime now = LocalDateTime.now();

        String conditionValue = StringUtils.hasText(productDTO.condition())
                ? productDTO.condition()
                : "new";

        Product product = new Product(
                null,
                productDTO.name(),
                productDTO.description(),
                new Price(productDTO.price()),
                new Condition(conditionValue),
                new Discount((float) productDTO.discount()),
                productDTO.brandId(),
                productDTO.subCategoryId(),
                null,
                now,
                now);

        // Search brand and subCategory
        Brand brand = brandRepository.findById(productDTO.brandId());

        if (brand == null) {
            throw new EntityNotFoundException("Brand", productDTO.brandId());
        }

        product.setBrand(brand);

        SubCategory subCategory = subCategoryRepository.findById(productDTO.subCategoryId());

        if (subCategory == null) {
            throw new EntityNotFoundException("SubCategory", productDTO.subCategoryId());
        }

        product.setSubCategory(subCategory);

        Product savedProduct = productRepository.save(product);

        // upload images
        if (productDTO.image() != null) {
            if (!productDTO.image().isEmpty()) {
                String url = fileManagerInterface.uploadFile(productDTO.image(), "/medias");
                Image image = new Image(null, new Url(url), true, savedProduct.getId());
                imageRepository.save(image);
                savedProduct.setImages(List.of(image));
            }
        }

        // save product specifications
        List<ProductSpecification> productSpecifications = new ArrayList<>();

        if (productDTO.specifications() != null) {
            for (ProductSpecificationDTO productSpecificationDTO : productDTO.specifications()) {
                ProductSpecification productSpecification = new ProductSpecification(null, savedProduct.getId(),
                        productSpecificationDTO.specificationId(), productSpecificationDTO.value(),
                        productSpecificationDTO.specificationValueId());

                productSpecification.setProduct(savedProduct);

                Specification specification = specificationRepository
                        .findById(productSpecificationDTO.specificationId());
                if (specification == null) {
                    throw new EntityNotFoundException("Specification", productSpecificationDTO.specificationId());
                }
                productSpecification.setSpecification(specification);

                if (productSpecificationDTO.specificationValueId() != null) {
                    SpecificationValue specificationValue = specificationValueRepository
                            .findById(productSpecificationDTO.specificationValueId());
                    if (specificationValue == null) {
                        throw new EntityNotFoundException("SpecificationValue",
                                productSpecificationDTO.specificationValueId());
                    }
                    productSpecification.setSpecificationValue(specificationValue);
                }

                ProductSpecification savedSpecificationValue = productSpecificationRepository
                        .save(productSpecification);
                productSpecifications.add(savedSpecificationValue);
            }
        }

        savedProduct.setProductSpecifications(productSpecifications);

        loggerPort.info(String.format("Product created successfully - ID: %d, Name: %s, Price: %.2f", 
            savedProduct.getId(), savedProduct.getName(), savedProduct.getPrice().getValue()));

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
    @Transactional
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
        if (!Objects.equals(product.getBrandId(), productDTO.brandId())) {
            product.setBrandId(productDTO.brandId());
        }
        if (!Objects.equals(product.getSubCategoryId(), productDTO.subCategoryId())) {
            product.setSubCategoryId(productDTO.subCategoryId());
        }

        product.setUpdatedAt(LocalDateTime.now());

        // Update specifications if provided
        if (productDTO.specifications() != null) {
            List<ProductSpecification> productSpecifications = new ArrayList<>();
            for (ProductSpecificationDTO productSpecificationDTO : productDTO.specifications()) {
                ProductSpecification ps = new ProductSpecification(
                        productSpecificationDTO.id(),
                        product.getId(),
                        productSpecificationDTO.specificationId(),
                        productSpecificationDTO.value(),
                        productSpecificationDTO.specificationValueId());

                if (productSpecificationDTO.specificationValueId() != null) {
                    SpecificationValue specificationValue = specificationValueRepository
                            .findById(productSpecificationDTO.specificationValueId());
                    if (specificationValue == null) {
                        throw new EntityNotFoundException("SpecificationValue",
                                productSpecificationDTO.specificationValueId());
                    }
                    ps.setSpecificationValue(specificationValue);
                }

                Specification specification = specificationRepository
                        .findById(productSpecificationDTO.specificationId());
                if (specification == null) {
                    throw new EntityNotFoundException("Specification", productSpecificationDTO.specificationId());
                }
                ps.setSpecification(specification);

                productSpecifications.add(ps);
            }
            product.setProductSpecifications(productSpecifications);
        }

        productRepository.save(product);

        return ProductResponseDTO.fromProduct(product);
    }

    @Override
    @Transactional
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
