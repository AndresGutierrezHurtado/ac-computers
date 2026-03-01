package com.accomputers.api.infrastructure.http.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accomputers.api.application.dtos.response.BrandResponseDTO;
import com.accomputers.api.application.ports.input.BrandServiceInterface;
import com.accomputers.api.infrastructure.http.responses.ResponseDTO;

@RestController
@RequestMapping("/brands")
public class BrandController {
    private final BrandServiceInterface brandServiceInterface;

    @Autowired
    public BrandController(BrandServiceInterface brandServiceInterface) {
        this.brandServiceInterface = brandServiceInterface;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<BrandResponseDTO>>> getAllBrands() {
        List<BrandResponseDTO> brands = brandServiceInterface.getAllBrands();

        ResponseDTO<List<BrandResponseDTO>> responseDTO = new ResponseDTO<>(
                "Brands retrieved successfully",
                true,
                brands);

        return ResponseEntity.ok(responseDTO);
    }
}
