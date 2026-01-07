package com.accomputers.api.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "specification_values")
public class SpecificationValueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "specification_id", nullable = false)
    private Integer specificationId;

    @Column(name = "value", nullable = false, columnDefinition = "TEXT")
    private String value;

    @Column(name = "specification_order", nullable = false)
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specification_id", insertable = false, updatable = false)
    private SpecificationEntity specification;

    public SpecificationValueEntity() {
    }

    public SpecificationValueEntity(Integer id, Integer specificationId, String value, Integer order) {
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

    public SpecificationEntity getSpecification() {
        return specification;
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

    public void setSpecification(SpecificationEntity specification) {
        this.specification = specification;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SpecificationValueEntity that = (SpecificationValueEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
