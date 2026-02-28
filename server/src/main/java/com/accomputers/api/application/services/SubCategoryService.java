package com.accomputers.api.application.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accomputers.api.application.dtos.response.SubCategoryDTO;
import com.accomputers.api.application.ports.input.SubCategoryServiceInterface;
import com.accomputers.api.application.ports.output.repositories.SubCategoryRepositoryInterface;
import com.accomputers.api.domain.entities.SubCategory;

@Service
public class SubCategoryService implements SubCategoryServiceInterface {
    private final SubCategoryRepositoryInterface subCategoryRepository;

    @Autowired
    public SubCategoryService(SubCategoryRepositoryInterface subCategoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
    }

    @Override
    public List<SubCategoryDTO> getAllSubCategories(Integer categoryId) {
        List<SubCategory> subCategories = categoryId != null
                ? subCategoryRepository.findByCategoryId(categoryId)
                : subCategoryRepository.findAll();

        return subCategories.stream()
                .map(SubCategoryDTO::fromSubCategory)
                .collect(Collectors.toList());
    }
}
