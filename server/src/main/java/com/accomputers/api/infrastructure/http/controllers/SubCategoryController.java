package com.accomputers.api.infrastructure.http.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accomputers.api.application.dtos.response.SubCategoryDTO;
import com.accomputers.api.application.ports.input.SubCategoryServiceInterface;
import com.accomputers.api.infrastructure.http.responses.ResponseDTO;

@RestController
@RequestMapping("/subcategories")
public class SubCategoryController {
    private final SubCategoryServiceInterface subCategoryServiceInterface;

    @Autowired
    public SubCategoryController(SubCategoryServiceInterface subCategoryServiceInterface) {
        this.subCategoryServiceInterface = subCategoryServiceInterface;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<SubCategoryDTO>>> getAllSubCategories(
            @RequestParam(required = false) Integer categoryId) {
        List<SubCategoryDTO> subCategories = subCategoryServiceInterface.getAllSubCategories(categoryId);

        ResponseDTO<List<SubCategoryDTO>> responseDTO = new ResponseDTO<>(
                "Subcategorías obtenidas exitosamente",
                true,
                subCategories);

        return ResponseEntity.ok(responseDTO);
    }
}
