package com.accomputers.api.domain.entities;

public class ProductStock {
    private Integer productId;
    private Integer stock;
    private Integer reserved;

    ProductStock(Integer productId, Integer stock, Integer reserved) {
        this.productId = productId;
        this.stock = stock;
        this.reserved = reserved;
    }

    // Getters
    public Integer getProductId() {
        return productId;
    }

    public Integer getStock() {
        return stock;
    }

    public Integer getReserved() {
        return reserved;
    }

    // Setters
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }
}
