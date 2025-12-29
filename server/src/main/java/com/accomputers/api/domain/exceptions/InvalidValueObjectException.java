package com.accomputers.api.domain.exceptions;

/**
 * Generic exception thrown when a value object is created with invalid data.
 */
public class InvalidValueObjectException extends DomainException {
    
    private final String valueObjectType;
    private final Object value;
    
    public InvalidValueObjectException(String message) {
        super(message);
        this.valueObjectType = null;
        this.value = null;
    }
    
    public InvalidValueObjectException(String valueObjectType, Object value, String reason) {
        super(String.format("Invalid %s value '%s': %s", valueObjectType, value, reason));
        this.valueObjectType = valueObjectType;
        this.value = value;
    }
    
    public String getValueObjectType() {
        return valueObjectType;
    }
    
    public Object getValue() {
        return value;
    }
}

