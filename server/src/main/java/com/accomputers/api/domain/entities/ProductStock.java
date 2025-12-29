package com.accomputers.api.domain.entities;

import java.time.LocalDateTime;

public class ProductStock {
    private Integer productId;
    private Integer stock;
    private Integer reserved;
    private LocalDateTime updatedAt;

    public ProductStock(Integer productId, Integer stock, Integer reserved, LocalDateTime updatedAt) {
        this.productId = productId;
        this.stock = stock;
        this.reserved = reserved;
        this.updatedAt = updatedAt;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
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
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
