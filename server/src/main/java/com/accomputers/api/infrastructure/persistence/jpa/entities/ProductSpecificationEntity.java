package com.accomputers.api.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "product_specifications")
public class ProductSpecificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specification_id", nullable = false)
    private SpecificationEntity specification;

    @Column(name = "value", columnDefinition = "TEXT")
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_value")
    private SpecificationValueEntity specificationValue;

    public ProductSpecificationEntity() {
    }

    public ProductSpecificationEntity(Integer id, ProductEntity product, SpecificationEntity specification,
                                     String value, SpecificationValueEntity specificationValue) {
        this.id = id;
        this.product = product;
        this.specification = specification;
        this.value = value;
        this.specificationValue = specificationValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public SpecificationEntity getSpecification() {
        return specification;
    }

    public void setSpecification(SpecificationEntity specification) {
        this.specification = specification;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SpecificationValueEntity getSpecificationValue() {
        return specificationValue;
    }

    public void setSpecificationValue(SpecificationValueEntity specificationValue) {
        this.specificationValue = specificationValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductSpecificationEntity that = (ProductSpecificationEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

