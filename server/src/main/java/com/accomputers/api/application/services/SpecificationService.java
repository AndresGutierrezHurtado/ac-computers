package com.accomputers.api.application.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.accomputers.api.application.dtos.response.SpecificationResponseDTO;
import com.accomputers.api.application.dtos.response.SpecificationValueResponseDTO;
import com.accomputers.api.application.ports.input.SpecificationServiceInterface;
import com.accomputers.api.application.ports.output.repositories.SpecificationRepositoryInterface;
import com.accomputers.api.application.ports.output.repositories.SpecificationValueRepositoryInterface;

@Service
public class SpecificationService implements SpecificationServiceInterface {
    private final SpecificationRepositoryInterface specificationRepository;
    private final SpecificationValueRepositoryInterface specificationValueRepository;

    public SpecificationService(
            SpecificationRepositoryInterface specificationRepository,
            SpecificationValueRepositoryInterface specificationValueRepository) {
        this.specificationRepository = specificationRepository;
        this.specificationValueRepository = specificationValueRepository;
    }

    @Override
    public List<SpecificationResponseDTO> getSpecifications(Integer subCategoryId) {
        return (subCategoryId == null
                ? specificationRepository.findAll()
                : specificationRepository.findBySubCategoryId(subCategoryId))
                        .stream()
                        .map(SpecificationResponseDTO::fromSpecification)
                        .collect(Collectors.toList());
    }

    @Override
    public List<SpecificationValueResponseDTO> getSpecificationValues(Integer specificationId) {
        return specificationValueRepository.findBySpecificationIdOrderByOrderAsc(specificationId)
                .stream()
                .map(SpecificationValueResponseDTO::fromSpecificationValue)
                .collect(Collectors.toList());
    }
}
