package com.accomputers.api.application.ports.output;

import com.accomputers.api.domain.entities.Product;
import java.util.List;

public interface ProductRecommendationInterface {
    List<Product> getRecommendations(String request);
}
