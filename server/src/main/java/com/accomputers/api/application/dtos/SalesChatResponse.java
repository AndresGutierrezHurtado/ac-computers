package com.accomputers.api.application.dtos;

import java.util.List;

public record SalesChatResponse(String message, List<ProductSearchHitDto> consultedProducts) {

    public SalesChatResponse {
        consultedProducts = consultedProducts == null ? List.of() : List.copyOf(consultedProducts);
    }
}
