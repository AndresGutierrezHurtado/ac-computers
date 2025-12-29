package com.accomputers.api.domain.entities;

public class SpecificationValue {
    private Integer id;
    private Integer specificationId;
    private String value;
    private Integer order;

    public SpecificationValue(Integer id, Integer specificationId, String value, Integer order) {
        this.id = id;
        this.specificationId = specificationId;
        this.value = value;
        this.order = order;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public Integer getSpecificationId() {
        return specificationId;
    }

    public String getValue() {
        return value;
    }

    public Integer getOrder() {
        return order;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setSpecificationId(Integer specificationId) {
        this.specificationId = specificationId;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
