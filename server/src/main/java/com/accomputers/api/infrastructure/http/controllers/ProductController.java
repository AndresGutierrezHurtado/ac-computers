package com.accomputers.api.infrastructure.http.controllers;

// Spring
import com.accomputers.api.application.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.MultiValueMap;

// Application
import com.accomputers.api.application.dtos.response.ProductAiOverviewResponse;
import com.accomputers.api.application.dtos.response.ProductResponseDTO;
import com.accomputers.api.application.ports.input.ProductServiceInterface;
import com.accomputers.api.infrastructure.http.responses.PaginatedResponseDTO;

// Infrastructure
import com.accomputers.api.infrastructure.http.responses.ResponseDTO;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductServiceInterface productServiceInterface;

    @Autowired
    public ProductController(ProductServiceInterface productServiceInterface) {
        this.productServiceInterface = productServiceInterface;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<ProductResponseDTO>> createProduct(
        @ModelAttribute @Valid createProductDTO productDTO,
        @RequestParam("images") List<MultipartFile> images,
        @RequestParam(value = "mainImageId", required = false) Integer mainImageId,
        @RequestParam(value = "mainImageIndex", required = false) Integer mainImageIndex
    ) {
        List<MultipartFile> resolvedImages = images != null ? images : productDTO.images();
        Integer resolvedMainImageId = mainImageId != null ? mainImageId : productDTO.mainImageId();
        Integer resolvedMainImageIndex = mainImageIndex != null ? mainImageIndex : productDTO.mainImageIndex();

        createProductDTO productDTOWithImage = new createProductDTO(
            productDTO.name(),
            productDTO.description(),
            productDTO.price(),
            productDTO.condition(),
            productDTO.discount(),
            productDTO.brandId(),
            productDTO.subCategoryId(),
            resolvedImages,
            null,
            resolvedMainImageId,
            resolvedMainImageIndex,
            productDTO.specifications()
        );
        
        ProductResponseDTO product = productServiceInterface.createProduct(productDTOWithImage);

        ResponseDTO<ProductResponseDTO> responseDTO = new ResponseDTO<>(
                "Product created successfully",
                true,
                product);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ProductResponseDTO>> getProductById(@PathVariable Integer id) {
        ProductResponseDTO product = productServiceInterface.getProductById(id);

        ResponseDTO<ProductResponseDTO> responseDTO = new ResponseDTO<>(
                "Product retrieved successfully",
                true,
                product);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}/overview")
    public ResponseEntity<ResponseDTO<ProductAiOverviewResponse>> getProductAiOverview(@PathVariable Integer id) {
        ProductAiOverviewResponse overview = productServiceInterface.getProductAiOverview(id);
        return ResponseEntity.ok(new ResponseDTO<>(
                "Product overview generated",
                true,
                overview));
    }

    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<ProductResponseDTO>> getAllProducts(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer perPage,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer subCategoryId,
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) Integer brandId,
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice,
            @RequestParam(required = false) Float minDiscount,
            @RequestParam(required = false) Float maxDiscount) {

        ProductFiltersDTO queryParams = new ProductFiltersDTO();
        queryParams.setPage(page);
        queryParams.setPerPage(perPage);
        queryParams.setSearch(search);
        queryParams.setCategoryId(categoryId);
        queryParams.setSubCategoryId(subCategoryId);
        queryParams.setCondition(condition);
        queryParams.setBrandId(brandId);
        queryParams.setMinPrice(minPrice);
        queryParams.setMaxPrice(maxPrice);
        queryParams.setMinDiscount(minDiscount);
        queryParams.setMaxDiscount(maxDiscount);

        PageDTO<ProductResponseDTO> products = productServiceInterface.getAllProducts(queryParams);

        PaginatedResponseDTO<ProductResponseDTO> responseDTO = new PaginatedResponseDTO<>(
                "Products retrieved successfully",
                true,
                products.total(),
                products.data());

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<ProductResponseDTO>> updateProduct(
            @PathVariable Integer id,
            @ModelAttribute createProductDTO productDTO,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "removeImageIds", required = false) List<Integer> removeImageIds,
            @RequestParam(value = "mainImageId", required = false) Integer mainImageId,
            @RequestParam(value = "mainImageIndex", required = false) Integer mainImageIndex,
            @RequestParam MultiValueMap<String, String> params) {
        List<MultipartFile> resolvedImages = images != null ? images : productDTO.images();
        List<Integer> resolvedRemoveImageIds = removeImageIds != null
                ? removeImageIds
                : productDTO.removeImageIds();
        Integer resolvedMainImageId = mainImageId != null ? mainImageId : productDTO.mainImageId();
        Integer resolvedMainImageIndex = mainImageIndex != null ? mainImageIndex : productDTO.mainImageIndex();

        if (resolvedRemoveImageIds == null || resolvedRemoveImageIds.isEmpty()) {
            resolvedRemoveImageIds = parseIntegerList(params.get("removeImageIds"));
            if (resolvedRemoveImageIds.isEmpty()) {
                resolvedRemoveImageIds = parseIntegerList(params.get("removeImageIds[]"));
            }
        }

        createProductDTO productDTOWithImage = new createProductDTO(
                productDTO.name(),
                productDTO.description(),
                productDTO.price(),
                productDTO.condition(),
                productDTO.discount(),
                productDTO.brandId(),
                productDTO.subCategoryId(),
                resolvedImages,
                resolvedRemoveImageIds,
                resolvedMainImageId,
                resolvedMainImageIndex,
                productDTO.specifications());

        ProductResponseDTO product = productServiceInterface.updateProduct(id, productDTOWithImage);

        ResponseDTO<ProductResponseDTO> responseDTO = new ResponseDTO<>(
                "Producto actualizado exitosamente",
                true,
                product);

        return ResponseEntity.ok(responseDTO);
    }

    private static List<Integer> parseIntegerList(List<String> values) {
        if (values == null || values.isEmpty()) {
            return List.of();
        }
        return values.stream()
                .filter(StringUtils::hasText)
                .map(String::trim)
                .map(value -> {
                    try {
                        return Integer.valueOf(value);
                    } catch (NumberFormatException ex) {
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteProduct(@PathVariable Integer id) {
        productServiceInterface.deleteProduct(id);

        ResponseDTO<Void> responseDTO = new ResponseDTO<>(
                "Producto eliminado exitosamente",
                true);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/sales-chat")
    public ResponseEntity<ResponseDTO<SalesChatResponse>> salesChat(@Valid @RequestBody SalesChatRequest request) {
        SalesChatResponse response = productServiceInterface.chat(request);
        return ResponseEntity.ok(new ResponseDTO<>(
                "Respuesta del asistente",
                true,
                response));
    }
}
