package com.accomputers.api.domain.entities;

import com.accomputers.api.domain.valueobjects.Stock;
import java.time.LocalDateTime;

public class ProductStock {
    private Integer productId;
    private Stock stock;
    private Integer reserved;
    private LocalDateTime updatedAt;

    public ProductStock(Integer productId, Stock stock, Integer reserved, LocalDateTime updatedAt) {
        this.productId = productId;
        this.stock = stock;
        this.reserved = reserved;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Integer getProductId() {
        return productId;
    }

    public Stock getStock() {
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

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
