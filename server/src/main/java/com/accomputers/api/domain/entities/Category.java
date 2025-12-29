package com.accomputers.api.domain.entities;

public class Category {
    private Integer id;
    private String name;
    private String slug;
    private String description;

    Category(Integer id, String name, String slug, String description) {
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

    public String getSlug() {
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

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
