package com.accomputers.api.application.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accomputers.api.application.dtos.response.CategoryResponseDTO;
import com.accomputers.api.application.ports.input.CategoryServiceInterface;
import com.accomputers.api.application.ports.output.repositories.CategoryRepositoryInterface;
import com.accomputers.api.domain.entities.Category;

@Service
public class CategoryService implements CategoryServiceInterface {
    private final CategoryRepositoryInterface categoryRepository;

    @Autowired
    public CategoryService(CategoryRepositoryInterface categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryResponseDTO::fromCategory)
                .collect(Collectors.toList());
    }
}
