package com.accomputers.api.domain.entities;

    import java.util.List;

public class Product {
    private Integer id;
    private String name;
    private String description;
    private Float price;
    private String condition;
    private Float discount;
    private Integer brandId;
    private Integer subCategoryId;
    private Brand brand;
    private List<Image> images;

    public Product(Integer id, String name, String description, Float price,  Integer brandId, Integer subCategoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.brandId = brandId;
        this.subCategoryId = subCategoryId;
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

    public Float getPrice() {
        return price;
    }

    public String getCondition() {
        return condition;
    }

    public Float getDiscount() {
        return discount;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public Integer getSubCategoryId() {
        return subCategoryId;
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

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setDiscount(Float discount) {
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
