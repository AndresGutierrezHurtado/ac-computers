package com.accomputers.api.domain.entities;

import com.accomputers.api.domain.valueobjects.Url;

public class Image {
    private Integer id;
    private Url url;
    private Boolean isMain;
    private Integer productId;
    private Product product;

    public Image(Integer id, Url url, Boolean isMain, Integer productId) {
        this.id = id;
        this.url = url;
        this.isMain = isMain;
        this.productId = productId;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public Url getUrl() {
        return url;
    }

    public Boolean getIsMain() {
        return isMain;
    }

    public Integer getProductId() {
        return productId;
    }

    public Product getProduct() {
        return product;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setUrl(Url url) {
        this.url = url;
    }

    public void setIsMain(Boolean isMain) {
        this.isMain = isMain;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
