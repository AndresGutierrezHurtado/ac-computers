package com.accomputers.api.domain.valueobjects;

import com.accomputers.api.domain.exceptions.InvalidValueObjectException;

public class Price {
    private final Float value;

    public Price(Float value) {
        if (value == null) {
            throw new InvalidValueObjectException("Price", value, "cannot be null");
        }
        if (value < 0) {
            throw new InvalidValueObjectException("Price", value, "cannot be negative");
        }
        this.value = value;
    }

    public Float getValue() {
        return value;
    }

    public Price applyDiscount(Discount discount) {
        if (discount == null) {
            return this;
        }
        Float discountValue = discount.getValue();
        Float discountedPrice = value * (1 - discountValue / 100.0f);
        return new Price(discountedPrice);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return value.equals(price.value);
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

