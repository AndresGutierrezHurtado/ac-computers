package com.accomputers.api.application.services;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.accomputers.api.domain.entities.Image;
// Domain
import com.accomputers.api.domain.entities.Product;
import com.accomputers.api.domain.entities.ProductSpecification;
import com.accomputers.api.domain.exceptions.EntityNotFoundException;
import com.accomputers.api.domain.valueobjects.Condition;
import com.accomputers.api.domain.valueobjects.Discount;
import com.accomputers.api.domain.valueobjects.Price;
import com.accomputers.api.domain.valueobjects.Url;
// Ports
import com.accomputers.api.application.ports.input.ProductServiceInterface;
import com.accomputers.api.application.ports.output.FileManagerInterface;
import com.accomputers.api.application.ports.output.ProductRecommendationInterface;
import com.accomputers.api.application.ports.output.repositories.ImageRepositoryInterface;
import com.accomputers.api.application.ports.output.repositories.ProductRepositoryInterface;
import com.accomputers.api.application.ports.output.repositories.ProductSpecificationRepositoryInterface;

// DTOs
import com.accomputers.api.application.dtos.createProductDTO;
import com.accomputers.api.application.dtos.createProductDTO.ImageDTO;
import com.accomputers.api.application.dtos.createProductDTO.ProductSpecificationDTO;
import com.accomputers.api.application.dtos.response.ProductResponseDTO;
import com.accomputers.api.application.dtos.PageDTO;
import com.accomputers.api.application.dtos.ProductCriteria;
import com.accomputers.api.application.dtos.ProductFiltersDTO;

@Service
public class ProductService implements ProductServiceInterface {
    private final ProductRepositoryInterface productRepository;
    private final ProductRecommendationInterface productRecommendationInterface;
    private final FileManagerInterface fileManagerInterface;
    private final ImageRepositoryInterface imageRepository;
    private final ProductSpecificationRepositoryInterface productSpecificationRepository;

    @Autowired
    public ProductService(
            ProductRepositoryInterface productRepository,
            ProductRecommendationInterface productRecommendationInterface,
            ImageRepositoryInterface imageRepository,
            ProductSpecificationRepositoryInterface productSpecificationRepository,
            FileManagerInterface fileManagerInterface) {
        this.productRepository = productRepository;
        this.productRecommendationInterface = productRecommendationInterface;
        this.imageRepository = imageRepository;
        this.productSpecificationRepository = productSpecificationRepository;
        this.fileManagerInterface = fileManagerInterface;
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

        // upload images
        if (productDTO.images() != null) {
            for (ImageDTO imageDTO : productDTO.images()) {
                if (imageDTO.file() != null && !imageDTO.file().isEmpty()) {
                    String url = fileManagerInterface.uploadFile(imageDTO.file(), "products");
                    Image image = new Image(null, new Url(url), imageDTO.isMain(), savedProduct.getId());
                    imageRepository.save(image);
                }
            }
        }

        // save product specifications
        if (productDTO.specifications() != null) {
            for (ProductSpecificationDTO productSpecificationDTO : productDTO.specifications()) {
                ProductSpecification productSpecification = new ProductSpecification(null, savedProduct.getId(), productSpecificationDTO.specificationId(), productSpecificationDTO.value(), productSpecificationDTO.specificationValueId());
                productSpecificationRepository.save(productSpecification);
            }
        }

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

        // Update images if provided
        if (productDTO.images() != null) {
            updateImages(savedProduct.getId(), productDTO.images());
        }

        // Update specifications if provided
        if (productDTO.specifications() != null) {
            updateSpecifications(savedProduct.getId(), productDTO.specifications());
        }

        return ProductResponseDTO.fromProduct(savedProduct);
    }

    private void updateImages(Integer productId, List<ImageDTO> imageDTOs) {
        // Get existing images
        List<Image> existingImages = imageRepository.findByProductId(productId);
        Set<Integer> existingImageIds = existingImages.stream()
                .map(Image::getId)
                .collect(Collectors.toSet());

        // Track which images are being kept/updated
        Set<Integer> updatedImageIds = new HashSet<>();

        // Process images from DTO
        for (ImageDTO imageDTO : imageDTOs) {
            if (imageDTO.id() != null && existingImageIds.contains(imageDTO.id())) {
                // Update existing image
                Image existingImage = existingImages.stream()
                        .filter(img -> img.getId().equals(imageDTO.id()))
                        .findFirst()
                        .orElse(null);

                if (existingImage != null) {
                    // If file is provided, upload new file and delete old one
                    if (imageDTO.file() != null && !imageDTO.file().isEmpty()) {
                        // Delete old file from storage
                        fileManagerInterface.deleteFile(existingImage.getUrl().getValue());
                        // Upload new file
                        String newUrl = fileManagerInterface.uploadFile(imageDTO.file(), "products");
                        existingImage.setUrl(new Url(newUrl));
                    }
                    // Update isMain flag
                    existingImage.setIsMain(imageDTO.isMain());
                    imageRepository.save(existingImage);
                    updatedImageIds.add(imageDTO.id());
                }
            } else {
                // Create new image
                if (imageDTO.file() != null && !imageDTO.file().isEmpty()) {
                    String url = fileManagerInterface.uploadFile(imageDTO.file(), "products");
                    Image newImage = new Image(null, new Url(url), imageDTO.isMain(), productId);
                    Image savedImage = imageRepository.save(newImage);
                    updatedImageIds.add(savedImage.getId());
                }
            }
        }

        // Delete images that are no longer in the DTO
        for (Image existingImage : existingImages) {
            if (!updatedImageIds.contains(existingImage.getId())) {
                // Delete file from storage
                fileManagerInterface.deleteFile(existingImage.getUrl().getValue());
                // Delete from database
                imageRepository.delete(existingImage.getId());
            }
        }
    }

    private void updateSpecifications(Integer productId, List<ProductSpecificationDTO> specificationDTOs) {
        // Get existing specifications
        List<ProductSpecification> existingSpecifications = productSpecificationRepository.findByProductId(productId);
        Set<Integer> existingSpecIds = existingSpecifications.stream()
                .map(ProductSpecification::getId)
                .collect(Collectors.toSet());

        // Track which specifications are being kept/updated
        Set<Integer> updatedSpecIds = new HashSet<>();

        // Process specifications from DTO
        for (ProductSpecificationDTO specDTO : specificationDTOs) {
            if (specDTO.id() != null && existingSpecIds.contains(specDTO.id())) {
                // Update existing specification
                ProductSpecification existingSpec = existingSpecifications.stream()
                        .filter(spec -> spec.getId().equals(specDTO.id()))
                        .findFirst()
                        .orElse(null);

                if (existingSpec != null) {
                    existingSpec.setSpecificationId(specDTO.specificationId());
                    existingSpec.setValue(specDTO.value());
                    existingSpec.setIdValue(specDTO.specificationValueId());
                    productSpecificationRepository.save(existingSpec);
                    updatedSpecIds.add(specDTO.id());
                }
            } else {
                // Create new specification
                ProductSpecification newSpec = new ProductSpecification(
                        null,
                        productId,
                        specDTO.specificationId(),
                        specDTO.value(),
                        specDTO.specificationValueId());
                ProductSpecification savedSpec = productSpecificationRepository.save(newSpec);
                updatedSpecIds.add(savedSpec.getId());
            }
        }

        // Delete specifications that are no longer in the DTO
        for (ProductSpecification existingSpec : existingSpecifications) {
            if (!updatedSpecIds.contains(existingSpec.getId())) {
                productSpecificationRepository.delete(existingSpec.getId());
            }
        }
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
