package com.accomputers.api.application.ports.output.repositories;

import com.accomputers.api.domain.entities.Product;
import java.util.List;

public interface ProductRepositoryInterface {
    List<Product> findAll();
    Product findById(Integer id);
    Product save(Product product);
    void delete(Integer id);
}
