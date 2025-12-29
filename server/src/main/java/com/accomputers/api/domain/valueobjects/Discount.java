package com.accomputers.api.domain.valueobjects;

public class Discount {
    private final Float value;

    public Discount(Float value) {
        if (value == null) {
            throw new IllegalArgumentException("Discount cannot be null");
        }
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }
        this.value = value;
    }

    public Float getValue() {
        return value;
    }

    public boolean hasDiscount() {
        return value > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discount discount = (Discount) o;
        return value.equals(discount.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

