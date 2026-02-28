package com.accomputers.api.application.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accomputers.api.application.dtos.response.BrandResponseDTO;
import com.accomputers.api.application.ports.input.BrandServiceInterface;
import com.accomputers.api.application.ports.output.repositories.BrandRepositoryInterface;
import com.accomputers.api.domain.entities.Brand;

@Service
public class BrandService implements BrandServiceInterface {
    private final BrandRepositoryInterface brandRepository;

    @Autowired
    public BrandService(BrandRepositoryInterface brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<BrandResponseDTO> getAllBrands() {
        List<Brand> brands = brandRepository.findAll();
        return brands.stream()
                .map(BrandResponseDTO::fromBrand)
                .collect(Collectors.toList());
    }
}
