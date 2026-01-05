package com.accomputers.api.application.ports.output.repositories;

import com.accomputers.api.domain.entities.SubCategory;
import java.util.List;

public interface SubCategoryRepositoryInterface {
    List<SubCategory> findAll();
    SubCategory findById(Integer id);
    SubCategory findBySlug(String slug);
    SubCategory findByName(String name);
    List<SubCategory> findByCategoryId(Integer categoryId);
    SubCategory save(SubCategory subCategory);
    boolean existsBySlug(String slug);
    boolean existsByName(String name);
}

