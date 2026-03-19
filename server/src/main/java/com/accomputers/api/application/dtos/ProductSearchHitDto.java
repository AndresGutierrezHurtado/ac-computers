package com.accomputers.api.application.dtos;

/**
 * Product row returned to the client when the sales chat tool searched the catalog.
 */
public record ProductSearchHitDto(
        Integer id,
        String name,
        Float price,
        String condition,
        String brand,
        String description) {
}
