package com.arpit.MusicApp.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when authentication fails.
 * Follows Single Responsibility Principle - handles only authentication failures.
 * 
 * Examples: Invalid credentials, Expired token, Invalid token
 */
public class AuthenticationException extends MusicAppException {
    
    private static final String ERROR_CODE = "AUTHENTICATION_FAILED";
    
    /**
     * Constructor with custom message
     * 
     * @param message Description of the authentication failure
     */
    public AuthenticationException(String message) {
        super(message, ERROR_CODE, HttpStatus.UNAUTHORIZED.value());
    }
    
    /**
     * Constructor with message and cause
     * 
     * @param message Description of the authentication failure
     * @param cause The underlying cause of the authentication failure
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause, ERROR_CODE, HttpStatus.UNAUTHORIZED.value());
    }
}
