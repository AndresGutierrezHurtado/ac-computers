package com.accomputers.api.infrastructure.http.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accomputers.api.application.dtos.response.CategoryResponseDTO;
import com.accomputers.api.application.ports.input.CategoryServiceInterface;
import com.accomputers.api.infrastructure.http.responses.ResponseDTO;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryServiceInterface categoryServiceInterface;

    @Autowired
    public CategoryController(CategoryServiceInterface categoryServiceInterface) {
        this.categoryServiceInterface = categoryServiceInterface;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<CategoryResponseDTO>>> getAllCategories() {
        List<CategoryResponseDTO> categories = categoryServiceInterface.getAllCategories();

        ResponseDTO<List<CategoryResponseDTO>> responseDTO = new ResponseDTO<>(
                "Categories retrieved successfully",
                true,
                categories);

        return ResponseEntity.ok(responseDTO);
    }
}
