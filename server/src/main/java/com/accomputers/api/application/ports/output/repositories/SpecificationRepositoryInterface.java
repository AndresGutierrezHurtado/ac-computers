package com.accomputers.api.application.ports.output.repositories;

import com.accomputers.api.domain.entities.Specification;
import java.util.List;

public interface SpecificationRepositoryInterface {
    List<Specification> findAll();
    Specification findById(Integer id);
    Specification findBySlug(String slug);
    Specification findByName(String name);
    List<Specification> findBySubCategoryId(Integer subCategoryId);
    List<Specification> findBySubCategoryIdAndIsFilterableTrue(Integer subCategoryId);
    List<Specification> findBySubCategoryIdAndIsMandatoryTrue(Integer subCategoryId);
    Specification save(Specification specification);
    boolean existsBySlug(String slug);
    boolean existsByName(String name);
}

