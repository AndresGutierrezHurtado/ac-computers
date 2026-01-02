package com.accomputers.api.application.dtos;

import com.accomputers.api.domain.valueobjects.Condition;

/**
 * Criteria for filtering and paginating products.
 * Follows hexagonal architecture principles by being in the application layer.
 */
public class ProductCriteria {
    private Integer page;
    private Integer perPage;
    private String search;
    private Integer categoryId;
    private Integer subCategoryId;
    private String condition;
    private Integer brandId;
    private Float minPrice;
    private Float maxPrice;
    private Float minDiscount;
    private Float maxDiscount;

    public ProductCriteria() {
        this.page = 1;
        this.perPage = 10;
    }

    public ProductCriteria(Integer page, Integer perPage, String search, Integer categoryId, 
                          Integer subCategoryId, String condition, Integer brandId, 
                          Float minPrice, Float maxPrice, Float minDiscount, Float maxDiscount) {
        this.page = page != null && page > 0 ? page : 1;
        this.perPage = perPage != null && perPage > 0 ? perPage : 10;
        this.search = search;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.condition = condition;
        this.brandId = brandId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minDiscount = minDiscount;
        this.maxDiscount = maxDiscount;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page != null && page > 0 ? page : 1;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage != null && perPage > 0 ? perPage : 10;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Integer subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Float minPrice) {
        this.minPrice = minPrice;
    }

    public Float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Float maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Float getMinDiscount() {
        return minDiscount;
    }

    public void setMinDiscount(Float minDiscount) {
        this.minDiscount = minDiscount;
    }

    public Float getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(Float maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    /**
     * Validates the condition value against valid Condition types.
     * @return true if condition is null or valid, false otherwise
     */
    public boolean isValidCondition() {
        if (condition == null || condition.trim().isEmpty()) {
            return true;
        }
        return Condition.ConditionType.fromString(condition) != null;
    }
}

