package com.accomputers.api.infrastructure.http.controllers;

// Spring
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.multipart.MultipartFile;

import com.accomputers.api.application.dtos.PageDTO;
// Application
import com.accomputers.api.application.dtos.ProductFiltersDTO;
import com.accomputers.api.application.dtos.createProductDTO;
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
        @ModelAttribute createProductDTO productDTO,
        @RequestParam("image") MultipartFile image
    ) {
        System.out.println(productDTO.toString());
        createProductDTO productDTOWithImage = new createProductDTO(
            productDTO.name(),
            productDTO.description(),
            productDTO.price(),
            productDTO.condition(),
            productDTO.discount(),
            productDTO.brandId(),
            productDTO.subCategoryId(),
            image,
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
                products.data(),
                products.total());

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<ProductResponseDTO>> updateProduct(
            @PathVariable Integer id,
            @ModelAttribute createProductDTO productDTO) {

        ProductResponseDTO product = productServiceInterface.updateProduct(id, productDTO);

        ResponseDTO<ProductResponseDTO> responseDTO = new ResponseDTO<>(
                "Product updated successfully",
                true,
                product);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteProduct(@PathVariable Integer id) {
        productServiceInterface.deleteProduct(id);

        ResponseDTO<Void> responseDTO = new ResponseDTO<>(
                "Product deleted successfully",
                true);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/recommendations")
    public ResponseEntity<ResponseDTO<String>> getProductRecommendations(@RequestBody String request) {
        String recommendations = productServiceInterface.getProductRecommendations(request);

        ResponseDTO<String> responseDTO = new ResponseDTO<>(
                "Product recommendations retrieved successfully",
                true,
                recommendations);

        return ResponseEntity.ok(responseDTO);
    }
}
