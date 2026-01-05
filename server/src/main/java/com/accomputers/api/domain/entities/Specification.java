package com.accomputers.api.domain.entities;

import com.accomputers.api.domain.valueobjects.Slug;

public class Specification {
    private Integer id;
    private String name;
    private Slug slug;
    private String type;
    private String unit;
    private Boolean isFilterable;
    private Boolean isMandatory;
    private Integer subCategoryId;

    public Specification(Integer id, String name, Slug slug, String type, String unit, Boolean isFilterable, Boolean isMandatory, Integer subCategoryId) {
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

    public Slug getSlug() {
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

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSlug(Slug slug) {
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
}
