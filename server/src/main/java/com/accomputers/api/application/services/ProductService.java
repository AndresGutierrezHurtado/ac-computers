package com.accomputers.api.application.services;

import com.accomputers.api.application.dtos.*;
import com.accomputers.api.application.ports.output.AIPort;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import reactor.core.publisher.Flux;

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
import com.accomputers.api.application.ports.output.repositories.*;

// DTOs
import com.accomputers.api.application.dtos.createProductDTO.ProductSpecificationDTO;
import com.accomputers.api.application.dtos.response.ProductAiOverviewResponse;
import com.accomputers.api.application.dtos.response.ProductResponseDTO;

@Service
public class ProductService implements ProductServiceInterface {
    private final ProductRepositoryInterface productRepository;
    private final FileManagerInterface fileManagerInterface;
    private final ImageRepositoryInterface imageRepository;
    private final ProductSpecificationRepositoryInterface productSpecificationRepository;
    private final BrandRepositoryInterface brandRepository;
    private final SubCategoryRepositoryInterface subCategoryRepository;
    private final SpecificationRepositoryInterface specificationRepository;
    private final SpecificationValueRepositoryInterface specificationValueRepository;
    private final AIPort aiPort;
    private final LoggerPort loggerPort;

    @Autowired
    public ProductService(
            ProductRepositoryInterface productRepository,
            ImageRepositoryInterface imageRepository,
            ProductSpecificationRepositoryInterface productSpecificationRepository,
            BrandRepositoryInterface brandRepository,
            SubCategoryRepositoryInterface subCategoryRepository,
            SpecificationRepositoryInterface specificationRepository,
            SpecificationValueRepositoryInterface specificationValueRepository,
            FileManagerInterface fileManagerInterface,
            AIPort aiPort,
            LoggerPort loggerPort) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.productSpecificationRepository = productSpecificationRepository;
        this.brandRepository = brandRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.specificationRepository = specificationRepository;
        this.specificationValueRepository = specificationValueRepository;
        this.fileManagerInterface = fileManagerInterface;
        this.aiPort = aiPort;
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
        List<MultipartFile> images = productDTO.images();
        List<MultipartFile> cleanedImages = images == null
                ? List.of()
                : images.stream()
                        .filter(file -> file != null && !file.isEmpty())
                        .collect(Collectors.toList());

        if (cleanedImages.isEmpty()) {
            throw new IllegalArgumentException("At least one image is required");
        }

        int mainIndex = productDTO.mainImageIndex() != null ? productDTO.mainImageIndex() : 0;
        if (mainIndex < 0 || mainIndex >= cleanedImages.size()) {
            mainIndex = 0;
        }

        List<Image> savedImages = new ArrayList<>();
        for (int i = 0; i < cleanedImages.size(); i++) {
            MultipartFile file = cleanedImages.get(i);
            String url = fileManagerInterface.uploadFile(file, "/medias");
            boolean isMain = i == mainIndex;
            Image image = new Image(null, new Url(url), isMain, savedProduct.getId());
            Image savedImage = imageRepository.save(image);
            savedImages.add(savedImage);
        }
        savedProduct.setImages(savedImages);

        // save product specifications
        List<ProductSpecification> productSpecifications = new ArrayList<>();

        if (productDTO.specifications() != null) {
            for (ProductSpecificationDTO productSpecificationDTO : productDTO.specifications()) {
                ProductSpecification productSpecification = new ProductSpecification(
                        null,
                        savedProduct.getId(),
                        productSpecificationDTO.specificationId(),
                        productSpecificationDTO.value(),
                        productSpecificationDTO.specificationValueId());

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

        productRepository.save(savedProduct);

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
    public Flux<ProductAiOverviewResponse> getProductAiOverview(Integer id) {
        ProductResponseDTO product = getProductById(id);
        String context = buildProductOverviewContext(product);
        return aiPort.generateProductOverview(context)
                .filter(StringUtils::hasText)
                .map(text -> new ProductAiOverviewResponse(text));
    }

    private static String buildProductOverviewContext(ProductResponseDTO p) {
        StringBuilder sb = new StringBuilder(768);
        sb.append("Datos del producto:\n");
        sb.append("- Nombre: ").append(p.name()).append('\n');
        if (StringUtils.hasText(p.description())) {
            sb.append("- Descripción: ").append(p.description()).append('\n');
        }
        if (p.brand() != null && StringUtils.hasText(p.brand().name())) {
            sb.append("- Marca: ").append(p.brand().name()).append('\n');
        }
        if (p.subCategory() != null && StringUtils.hasText(p.subCategory().name())) {
            sb.append("- Subcategoría: ").append(p.subCategory().name()).append('\n');
        }
        if (p.price() != null) {
            sb.append("- Precio (COP): ").append(p.price()).append('\n');
        }
        if (p.discount() != null && p.discount() > 0) {
            sb.append("- Descuento %: ").append(p.discount()).append('\n');
        }
        if (StringUtils.hasText(p.condition())) {
            sb.append("- Condición: ").append(p.condition()).append('\n');
        }
        if (p.productSpecifications() != null && !p.productSpecifications().isEmpty()) {
            sb.append("- Especificaciones:\n");
            for (var ps : p.productSpecifications()) {
                if (ps == null) {
                    continue;
                }
                String specName = ps.specification() != null && StringUtils.hasText(ps.specification().name())
                        ? ps.specification().name()
                        : "Especificación";
                String val = StringUtils.hasText(ps.value()) ? ps.value() : "";
                sb.append("  · ").append(specName).append(": ").append(val).append('\n');
            }
        }
        return sb.toString();
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
    @CacheEvict(cacheNames = "productAiOverviews", key = "#id")
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

        List<Image> existingImages = imageRepository.findByProductId(product.getId());
        Map<Integer, Image> existingById = existingImages.stream()
                .filter(img -> img.getId() != null)
                .collect(Collectors.toMap(Image::getId, img -> img, (a, b) -> a));

        // Remove selected images
        List<Integer> removeImageIds = productDTO.removeImageIds();
        if (removeImageIds != null && !removeImageIds.isEmpty()) {
            for (Integer imageId : removeImageIds) {
                Image img = existingById.get(imageId);
                if (img == null) {
                    continue;
                }
                if (img.getUrl() != null && img.getUrl().getValue() != null) {
                    fileManagerInterface.deleteFile(img.getUrl().getValue());
                }
                imageRepository.delete(imageId);
                existingImages.remove(img);
                existingById.remove(imageId);
            }
        }

        boolean mainSelectionProvided = productDTO.mainImageId() != null || productDTO.mainImageIndex() != null;
        if (mainSelectionProvided) {
            imageRepository.setAllImagesAsNotMainByProductId(product.getId());
            for (Image img : existingImages) {
                img.setIsMain(false);
            }
        }

        // Upload new images
        List<MultipartFile> incomingImages = productDTO.images();
        List<MultipartFile> cleanedImages = incomingImages == null
                ? List.of()
                : incomingImages.stream()
                        .filter(file -> file != null && !file.isEmpty())
                        .collect(Collectors.toList());

        List<Image> savedNewImages = new ArrayList<>();
        Integer mainImageIndex = productDTO.mainImageIndex();
        for (int i = 0; i < cleanedImages.size(); i++) {
            MultipartFile file = cleanedImages.get(i);
            boolean isMain = mainImageIndex != null && mainImageIndex == i;
            String url = fileManagerInterface.uploadFile(file, "/medias");
            Image image = new Image(null, new Url(url), isMain, product.getId());
            Image savedImage = imageRepository.save(image);
            savedNewImages.add(savedImage);
        }

        // Mark an existing image as main if requested
        Integer mainImageId = productDTO.mainImageId();
        if (mainImageId != null) {
            Image mainImage = imageRepository.findById(mainImageId);
            if (mainImage == null) {
                throw new EntityNotFoundException("Image", mainImageId);
            }
            if (!Objects.equals(mainImage.getProductId(), product.getId())) {
                throw new IllegalArgumentException("Image does not belong to the product");
            }
            mainImage.setIsMain(true);
            imageRepository.save(mainImage);
            for (Image img : existingImages) {
                if (Objects.equals(img.getId(), mainImageId)) {
                    img.setIsMain(true);
                } else if (mainSelectionProvided) {
                    img.setIsMain(false);
                }
            }
        }

        // Ensure at least one main image exists when there are images
        List<Image> finalImages = new ArrayList<>();
        finalImages.addAll(existingImages);
        finalImages.addAll(savedNewImages);
        boolean hasMain = finalImages.stream().anyMatch(img -> Boolean.TRUE.equals(img.getIsMain()));
        if (!hasMain && !finalImages.isEmpty()) {
            imageRepository.setAllImagesAsNotMainByProductId(product.getId());
            Image fallback = finalImages.get(0);
            fallback.setIsMain(true);
            imageRepository.save(fallback);
        }

        product.setImages(finalImages);

        loggerPort.info(String.format("Product updated successfully - ID: %d, Name: %s, Price: %.2f",
                product.getId(), product.getName(), product.getPrice().getValue()));

        return ProductResponseDTO.fromProduct(product);
    }

    @Override
    @CacheEvict(cacheNames = "productAiOverviews", key = "#id")
    @Transactional
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id);

        if (product == null) {
            throw new EntityNotFoundException("Product", id);
        }

        loggerPort.info(String.format("Product deleted successfully - ID: %d, Name: %s",
                product.getId(), product.getName()));

        productRepository.delete(id);
    }

    @Override
    public Flux<SalesChatResponse> chat(SalesChatRequest request) {
        return aiPort.chat(request.messages())
                .filter(StringUtils::hasText)
                .map(chunk -> new SalesChatResponse(chunk, List.of()));
    }
}
