package com.accomputers.api.domain.entities;

public class StockNotification {
    private Integer productId;
    private String email;
    private Boolean isNotified;

    // Getters
    public Integer getProductId() {
        return productId;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getIsNotified() {
        return isNotified;
    }

    // Setters
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIsNotified(Boolean isNotified) {
        this.isNotified = isNotified;
    }
}
