package com.accomputers.api.domain.exceptions;

/**
 * Exception thrown when attempting to create a user with an email that already exists in the system.
 */
public class EmailAlreadyExistsException extends DomainException {
    
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
    
    public static EmailAlreadyExistsException forEmail(String email) {
        return new EmailAlreadyExistsException(
            String.format("Email '%s' is already registered", email)
        );
    }
}

