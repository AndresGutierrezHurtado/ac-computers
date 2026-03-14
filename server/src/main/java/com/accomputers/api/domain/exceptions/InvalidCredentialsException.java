package com.accomputers.api.domain.exceptions;

/**
 * Thrown when authentication fails. Uses a single generic message so callers cannot infer
 * whether the email exists or only the password was wrong.
 */
public class InvalidCredentialsException extends DomainException {

    private static final String MESSAGE = "Invalid email or password";

    public InvalidCredentialsException() {
        super(MESSAGE);
    }

    public static InvalidCredentialsException invalidCredentials() {
        return new InvalidCredentialsException();
    }
}
