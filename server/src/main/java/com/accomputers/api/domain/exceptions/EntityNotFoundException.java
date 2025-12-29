package com.accomputers.api.domain.exceptions;

/**
 * Generic exception thrown when an entity is not found in the system.
 */
public class EntityNotFoundException extends DomainException {
    
    private final String entityType;
    private final Object identifier;
    
    public EntityNotFoundException(String message) {
        super(message);
        this.entityType = null;
        this.identifier = null;
    }
    
    public EntityNotFoundException(String entityType, Object identifier) {
        super(String.format("%s with identifier '%s' not found", entityType, identifier));
        this.entityType = entityType;
        this.identifier = identifier;
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    public Object getIdentifier() {
        return identifier;
    }
}

