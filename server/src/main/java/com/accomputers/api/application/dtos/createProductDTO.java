package com.accomputers.api.application.dtos;

public record createProductDTO(String name, String description, double price, double discount, int categoryId, int brandId, int subCategoryId) {
}
