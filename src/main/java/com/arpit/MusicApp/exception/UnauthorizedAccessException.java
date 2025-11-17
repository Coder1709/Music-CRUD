package com.arpit.MusicApp.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when user is not authorized to access a resource.
 * Follows Single Responsibility Principle - handles only authorization failures.
 * 
 * Examples: User trying to access another user's playlist, 
 * Non-admin trying to perform admin operations
 */
public class UnauthorizedAccessException extends MusicAppException {
    
    private static final String ERROR_CODE = "UNAUTHORIZED_ACCESS";
    
    /**
     * Constructor with custom message
     * 
     * @param message Description of the unauthorized action
     */
    public UnauthorizedAccessException(String message) {
        super(message, ERROR_CODE, HttpStatus.FORBIDDEN.value());
    }
    
    /**
     * Default constructor with standard message
     */
    public UnauthorizedAccessException() {
        super("You are not authorized to perform this action", ERROR_CODE, HttpStatus.FORBIDDEN.value());
    }
}
