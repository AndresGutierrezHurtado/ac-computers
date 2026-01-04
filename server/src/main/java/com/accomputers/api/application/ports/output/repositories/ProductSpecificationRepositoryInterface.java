package com.accomputers.api.application.ports.output.repositories;

import com.accomputers.api.domain.entities.ProductSpecification;
import java.util.List;

public interface ProductSpecificationRepositoryInterface {
    ProductSpecification save(ProductSpecification productSpecification);
    List<ProductSpecification> findByProductId(Integer productId);
    void deleteByProductId(Integer productId);
    void delete(Integer id);
}

