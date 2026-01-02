package com.accomputers.api.infrastructure.http.controllers;

// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Application
import com.accomputers.api.application.dtos.createProductDTO;
import com.accomputers.api.application.dtos.response.ProductResponseDTO;
import com.accomputers.api.application.ports.input.ProductServiceInterface;

// Infrastructure
import com.accomputers.api.infrastructure.http.ResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductServiceInterface productServiceInterface;

    @Autowired
    public ProductController(ProductServiceInterface productServiceInterface) {
        this.productServiceInterface = productServiceInterface;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<ProductResponseDTO>> createProduct(@RequestBody createProductDTO productDTO) {
        ProductResponseDTO product = productServiceInterface.createProduct(productDTO);

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
    public ResponseEntity<ResponseDTO<List<ProductResponseDTO>>> getAllProducts() {
        List<ProductResponseDTO> products = productServiceInterface.getAllProducts();

        ResponseDTO<List<ProductResponseDTO>> responseDTO = new ResponseDTO<>(
                "Products retrieved successfully",
                true,
                products,
                (long) products.size());

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<ProductResponseDTO>> updateProduct(
            @PathVariable Integer id,
            @RequestBody createProductDTO productDTO) {

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

