package com.accomputers.api.domain.exceptions;

/**
 * Exception thrown when there is insufficient stock to fulfill a request.
 */
public class InsufficientStockException extends DomainException {
    
    private final Integer requested;
    private final Integer available;
    
    public InsufficientStockException(String message) {
        super(message);
        this.requested = null;
        this.available = null;
    }
    
    public InsufficientStockException(Integer productId, Integer requested, Integer available) {
        super(String.format("Insufficient stock for product %d. Requested: %d, Available: %d", 
            productId, requested, available));
        this.requested = requested;
        this.available = available;
    }
    
    public Integer getRequested() {
        return requested;
    }
    
    public Integer getAvailable() {
        return available;
    }
}

