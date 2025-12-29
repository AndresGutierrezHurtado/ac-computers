package com.accomputers.api.domain.entities;

public class ProductSpecification {
    private Integer productId;
    private Integer specificationId;
    private String value;
    private Integer idValue;
    private Product product;
    private Specification specification;
    private SpecificationValue specificationValue;

    public ProductSpecification(Integer productId, Integer specificationId, String value, Integer idValue) {
        this.productId = productId;
        this.specificationId = specificationId;
        this.value = value;
        this.idValue = idValue;
    }

    // Getters
    public Integer getProductId() {
        return productId;
    }

    public Integer getSpecificationId() {
        return specificationId;
    }

    public String getValue() {
        return value;
    }

    public Integer getIdValue() {
        return idValue;
    }

    public Product getProduct() {
        return product;
    }

    public Specification getSpecification() {
        return specification;
    }

    public SpecificationValue getSpecificationValue() {
        return specificationValue;
    }

    // Setters
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setSpecificationId(Integer specificationId) {
        this.specificationId = specificationId;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setIdValue(Integer idValue) {
        this.idValue = idValue;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public void setSpecificationValue(SpecificationValue specificationValue) {
        this.specificationValue = specificationValue;
    }
}
