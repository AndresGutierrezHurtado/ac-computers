package com.accomputers.api.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "product_specifications")
public class ProductSpecificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "specification_id", nullable = false)
    private Integer specificationId;

    @Column(name = "value", columnDefinition = "TEXT", nullable = true)
    private String value;

    @Column(name = "id_value", nullable = true)
    private Integer specificationValueId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specification_id", insertable = false, updatable = false)
    private SpecificationEntity specification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_value", insertable = false, updatable = false)
    private SpecificationValueEntity specificationValue;

    public ProductSpecificationEntity() {
    }

    public ProductSpecificationEntity(Integer id, Integer productId, Integer specificationId,
            String value, Integer specificationValueId) {
        this.id = id;
        this.productId = productId;
        this.specificationId = specificationId;
        this.value = value;
        this.specificationValueId = specificationValueId;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public Integer getProductId() {
        return productId;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public Integer getSpecificationId() {
        return specificationId;
    }

    public SpecificationEntity getSpecification() {
        return specification;
    }

    public String getValue() {
        return value;
    }

    public Integer getSpecificationValueId() {
        return specificationValueId;
    }

    public SpecificationValueEntity getSpecificationValue() {
        return specificationValue;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public void setSpecificationId(Integer specificationId) {
        this.specificationId = specificationId;
    }

    public void setSpecification(SpecificationEntity specification) {
        this.specification = specification;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setSpecificationValueId(Integer specificationValueId) {
        this.specificationValueId = specificationValueId;
    }

    public void setSpecificationValue(SpecificationValueEntity specificationValue) {
        this.specificationValue = specificationValue;
    }

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProductSpecificationEntity that = (ProductSpecificationEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
