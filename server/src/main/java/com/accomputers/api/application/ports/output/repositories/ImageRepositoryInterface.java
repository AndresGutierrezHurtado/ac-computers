package com.accomputers.api.application.ports.output.repositories;

import com.accomputers.api.domain.entities.Image;
import java.util.List;

public interface ImageRepositoryInterface {
    Image save(Image image);
    Image findById(Integer id);
    List<Image> findByProductId(Integer productId);
    void deleteByProductId(Integer productId);
    void delete(Integer id);
    void setAllImagesAsNotMainByProductId(Integer productId);
}

