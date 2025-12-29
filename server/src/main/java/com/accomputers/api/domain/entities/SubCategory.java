package com.accomputers.api.domain.entities;

import com.accomputers.api.domain.valueobjects.Slug;

public class SubCategory {
    private Integer id;
    private String name;
    private Slug slug;
    private String description;
    private Integer categoryId;
    private Category category;

    public SubCategory(Integer id, String name, Slug slug, String description, Integer categoryId) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.categoryId = categoryId;
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

    public String getDescription() {
        return description;
    }

    public Integer getCategoryId() {
        return categoryId;
    }
    
    public Category getCategory() {
        return category;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
