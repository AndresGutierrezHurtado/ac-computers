package com.accomputers.api.application.ports.output.repositories;

import com.accomputers.api.domain.entities.Brand;
import java.util.List;

public interface BrandRepositoryInterface {
    List<Brand> findAll();
    Brand findById(Integer id);
    Brand findByName(String name);
    Brand save(Brand brand);
    boolean existsByName(String name);
}

