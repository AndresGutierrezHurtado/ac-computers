package com.accomputers.api.application.ports.output.repositories;

import com.accomputers.api.domain.entities.SpecificationValue;
import java.util.List;

public interface SpecificationValueRepositoryInterface {
    List<SpecificationValue> findAll();
    SpecificationValue findById(Integer id);
    List<SpecificationValue> findBySpecificationId(Integer specificationId);
    List<SpecificationValue> findBySpecificationIdOrderByOrderAsc(Integer specificationId);
    SpecificationValue findBySpecificationIdAndValue(Integer specificationId, String value);
    SpecificationValue save(SpecificationValue specificationValue);
}

