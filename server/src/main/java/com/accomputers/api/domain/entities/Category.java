package com.accomputers.api.domain.entities;

import com.accomputers.api.domain.valueobjects.Slug;

public class Category {
    private Integer id;
    private String name;
    private Slug slug;
    private String description;

    public Category(Integer id, String name, Slug slug, String description) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
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
}
