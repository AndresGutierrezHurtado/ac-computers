package com.accomputers.api.infrastructure.http.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accomputers.api.application.dtos.response.SpecificationResponseDTO;
import com.accomputers.api.application.dtos.response.SpecificationValueResponseDTO;
import com.accomputers.api.application.ports.input.SpecificationServiceInterface;
import com.accomputers.api.infrastructure.http.responses.ResponseDTO;

@RestController
@RequestMapping
public class SpecificationController {
    private final SpecificationServiceInterface specificationServiceInterface;

    @Autowired
    public SpecificationController(SpecificationServiceInterface specificationServiceInterface) {
        this.specificationServiceInterface = specificationServiceInterface;
    }

    @GetMapping("/specifications")
    public ResponseEntity<ResponseDTO<List<SpecificationResponseDTO>>> getSpecifications(
            @RequestParam(required = false) Integer subCategoryId) {
        List<SpecificationResponseDTO> specs = specificationServiceInterface.getSpecifications(subCategoryId);

        ResponseDTO<List<SpecificationResponseDTO>> responseDTO = new ResponseDTO<>(
                "Specifications retrieved successfully",
                true,
                specs);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/specification-values")
    public ResponseEntity<ResponseDTO<List<SpecificationValueResponseDTO>>> getSpecificationValues(
            @RequestParam Integer specificationId) {
        List<SpecificationValueResponseDTO> values = specificationServiceInterface.getSpecificationValues(specificationId);

        ResponseDTO<List<SpecificationValueResponseDTO>> responseDTO = new ResponseDTO<>(
                "Specification values retrieved successfully",
                true,
                values);

        return ResponseEntity.ok(responseDTO);
    }
}
