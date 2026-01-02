package com.accomputers.api.application.dtos;

/**
 * DTO for receiving product query parameters from HTTP requests.
 * This DTO is used in the infrastructure layer (controllers) and then
 * transformed to ProductCriteria in the application layer.
 */
public class ProductQueryParamsDTO {
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

    public ProductQueryParamsDTO() {
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
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
     * Transforms this DTO to ProductCriteria.
     * @return ProductCriteria instance with the same values
     */
    public ProductCriteria toProductCriteria() {
        return new ProductCriteria(
            this.page,
            this.perPage,
            this.search,
            this.categoryId,
            this.subCategoryId,
            this.condition,
            this.brandId,
            this.minPrice,
            this.maxPrice,
            this.minDiscount,
            this.maxDiscount
        );
    }
}

