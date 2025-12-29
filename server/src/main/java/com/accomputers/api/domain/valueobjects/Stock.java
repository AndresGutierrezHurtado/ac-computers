package com.accomputers.api.domain.valueobjects;

public class Stock {
    private final Integer value;

    public Stock(Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("Stock cannot be null");
        }
        if (value < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public boolean isAvailable() {
        return value > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return value.equals(stock.value);
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
