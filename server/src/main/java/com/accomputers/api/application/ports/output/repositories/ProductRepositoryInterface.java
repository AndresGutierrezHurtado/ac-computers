package com.accomputers.api.application.ports.output.repositories;

import com.accomputers.api.application.dtos.PageDTO;
import com.accomputers.api.application.dtos.ProductCriteria;
import com.accomputers.api.domain.entities.Product;

public interface ProductRepositoryInterface {
    PageDTO<Product> findAll(ProductCriteria productCriteria);
    Product findById(Integer id);
    Product save(Product product);
    void delete(Integer id);
}
