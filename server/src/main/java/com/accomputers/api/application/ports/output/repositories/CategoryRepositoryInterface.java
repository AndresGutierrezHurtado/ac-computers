package com.accomputers.api.application.ports.output.repositories;

import com.accomputers.api.domain.entities.Category;
import java.util.List;

public interface CategoryRepositoryInterface {
    List<Category> findAll();
    Category findById(Integer id);
    Category findBySlug(String slug);
    Category findByName(String name);
    Category save(Category category);
    boolean existsBySlug(String slug);
    boolean existsByName(String name);
}
