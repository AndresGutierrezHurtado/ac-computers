package com.accomputers.api.domain.valueobjects;

public class Slug {
    private final String value;

    public Slug(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Slug cannot be null or empty");
        }
        String trimmed = value.trim().toLowerCase();
        if (!isValidSlug(trimmed)) {
            throw new IllegalArgumentException("Slug must contain only lowercase letters, numbers, and hyphens");
        }
        this.value = trimmed;
    }

    private boolean isValidSlug(String slug) {
        return slug.matches("^[a-z0-9]+(?:-[a-z0-9]+)*$");
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slug slug = (Slug) o;
        return value.equals(slug.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
