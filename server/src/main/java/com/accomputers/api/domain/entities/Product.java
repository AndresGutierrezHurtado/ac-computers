package com.accomputers.api.domain.entities;

import com.accomputers.api.domain.valueobjects.Discount;
import com.accomputers.api.domain.valueobjects.Price;
import java.time.LocalDateTime;
import java.util.List;

public class Product {
    private Integer id;
    private String name;
    private String description;
    private Price price;
    private String condition;
    private Discount discount;
    private Integer brandId;
    private Integer subCategoryId;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Brand brand;
    private List<Image> images;

    public Product(Integer id, String name, String description, Price price, String condition, Discount discount,
            Integer brandId, Integer subCategoryId, LocalDateTime deletedAt, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.condition = condition;
        this.discount = discount;
        this.brandId = brandId;
        this.subCategoryId = subCategoryId;
        this.deletedAt = deletedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Price getPrice() {
        return price;
    }

    public String getCondition() {
        return condition;
    }

    public Discount getDiscount() {
        return discount;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public Integer getSubCategoryId() {
        return subCategoryId;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Brand getBrand() {
        return brand;
    }

    public List<Image> getImages() {
        return images;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public void setSubCategoryId(Integer subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
