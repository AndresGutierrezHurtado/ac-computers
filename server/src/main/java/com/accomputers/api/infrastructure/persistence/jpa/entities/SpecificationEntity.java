package com.accomputers.api.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "specifications")
public class SpecificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "specification_type", nullable = false)
    private String type;

    @Column(name = "unit")
    private String unit;

    @Column(name = "is_filterable", nullable = false)
    private Boolean isFilterable;

    @Column(name = "is_mandatory", nullable = false)
    private Boolean isMandatory;

    @Column(name = "sub_category_id", nullable = false)
    private Integer subCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", insertable = false, updatable = false)
    private SubCategoryEntity subCategory;

    public SpecificationEntity() {
    }

    public SpecificationEntity(Integer id, String name, String slug, String type, String unit,
            Boolean isFilterable, Boolean isMandatory, Integer subCategoryId) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.type = type;
        this.unit = unit;
        this.isFilterable = isFilterable;
        this.isMandatory = isMandatory;
        this.subCategoryId = subCategoryId;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getType() {
        return type;
    }

    public String getUnit() {
        return unit;
    }

    public Boolean getIsFilterable() {
        return isFilterable;
    }

    public Boolean getIsMandatory() {
        return isMandatory;
    }

    public Integer getSubCategoryId() {
        return subCategoryId;
    }

    public SubCategoryEntity getSubCategory() {
        return subCategory;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setIsFilterable(Boolean isFilterable) {
        this.isFilterable = isFilterable;
    }

    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public void setSubCategoryId(Integer subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public void setSubCategory(SubCategoryEntity subCategory) {
        this.subCategory = subCategory;
    }

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SpecificationEntity that = (SpecificationEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
