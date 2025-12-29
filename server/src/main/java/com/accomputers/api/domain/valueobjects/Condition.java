package com.accomputers.api.domain.valueobjects;

import com.accomputers.api.domain.exceptions.InvalidValueObjectException;

/**
 * Value object representing the condition of a product.
 * Valid values: NEW, USED, REFURBISHED, FOR_PARTS
 */
public class Condition {
    
    public enum ConditionType {
        NEW("new"),
        USED("used"),
        REFURBISHED("refurbished"),
        FOR_PARTS("for_parts");
        
        private final String value;
        
        ConditionType(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public static ConditionType fromString(String value) {
            if (value == null) {
                return null;
            }
            String normalized = value.trim().toLowerCase();
            for (ConditionType type : ConditionType.values()) {
                if (type.value.equals(normalized)) {
                    return type;
                }
            }
            return null;
        }
    }
    
    private final ConditionType value;
    
    public Condition(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidValueObjectException("Condition", value, "cannot be null or empty");
        }
        
        ConditionType conditionType = ConditionType.fromString(value);
        if (conditionType == null) {
            throw new InvalidValueObjectException(
                "Condition", 
                value, 
                String.format("must be one of: %s, %s, %s, %s", 
                    ConditionType.NEW.getValue(),
                    ConditionType.USED.getValue(),
                    ConditionType.REFURBISHED.getValue(),
                    ConditionType.FOR_PARTS.getValue())
            );
        }
        this.value = conditionType;
    }
    
    public Condition(ConditionType conditionType) {
        if (conditionType == null) {
            throw new InvalidValueObjectException("Condition", null, "cannot be null");
        }
        this.value = conditionType;
    }
    
    public String getValue() {
        return value.getValue();
    }
    
    public ConditionType getConditionType() {
        return value;
    }
    
    public boolean isNew() {
        return value == ConditionType.NEW;
    }
    
    public boolean isUsed() {
        return value == ConditionType.USED;
    }
    
    public boolean isRefurbished() {
        return value == ConditionType.REFURBISHED;
    }
    
    public boolean isForParts() {
        return value == ConditionType.FOR_PARTS;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Condition condition = (Condition) o;
        return value == condition.value;
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
    
    @Override
    public String toString() {
        return value.getValue();
    }
}

