package com.accomputers.api.domain.valueobjects;

import java.net.URI;
import java.net.URISyntaxException;

public class Url {
    private final String value;

    public Url(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }
        String trimmed = value.trim();
        if (!isValidUrl(trimmed)) {
            throw new IllegalArgumentException("Invalid URL format");
        }
        this.value = trimmed;
    }

    private boolean isValidUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            // Also check for relative URLs (starting with /)
            return url.startsWith("/") && url.length() > 1;
        }
    }

    public String getValue() {
        return value;
    }

    public boolean isAbsolute() {
        try {
            URI uri = new URI(value);
            return uri.isAbsolute();
        } catch (URISyntaxException e) {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Url url = (Url) o;
        return value.equals(url.value);
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

