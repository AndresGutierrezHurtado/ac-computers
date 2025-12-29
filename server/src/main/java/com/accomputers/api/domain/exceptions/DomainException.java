package com.accomputers.api.domain.exceptions;

/**
 * Base exception for all domain-related exceptions.
 * This class provides a common structure for domain exceptions.
 */
public class DomainException extends RuntimeException {
    
    public DomainException(String message) {
        super(message);
    }
    
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}

