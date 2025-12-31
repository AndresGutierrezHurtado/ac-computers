package com.accomputers.api.domain.valueobjects;

import com.accomputers.api.domain.exceptions.InvalidValueObjectException;

public class Email {
    private final String value;

    public Email(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidValueObjectException("Email", value, "cannot be null or empty");
        }
        if (!isValidEmail(value)) {
            throw new InvalidValueObjectException("Email", value, "must be a valid email address");
        }
        this.value = value.trim().toLowerCase();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return value.equals(email.value);
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
