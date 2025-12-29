package com.accomputers.api.domain.entities;

import java.time.LocalDateTime;

public class StockNotification {
    private Integer id;
    private Integer productId;
    private String email;
    private Boolean isNotified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public StockNotification(Integer id, Integer productId, String email, Boolean isNotified, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.productId = productId;
        this.email = email;
        this.isNotified = isNotified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getIsNotified() {
        return isNotified;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIsNotified(Boolean isNotified) {
        this.isNotified = isNotified;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
