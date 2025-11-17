package com.arpit.MusicApp.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when business logic validation fails.
 * Follows Single Responsibility Principle - handles only business rule violations.
 * 
 * Examples: Duplicate username, Invalid song duration, 
 * Attempting to add same song to playlist twice
 */
public class BusinessValidationException extends MusicAppException {
    
    private static final String ERROR_CODE_PREFIX = "BUSINESS_VALIDATION";
    
    /**
     * Constructor with custom message
     * 
     * @param message Description of the validation failure
     */
    public BusinessValidationException(String message) {
        super(message, ERROR_CODE_PREFIX, HttpStatus.BAD_REQUEST.value());
    }
    
    /**
     * Constructor with custom message and specific error code
     * 
     * @param message Description of the validation failure
     * @param specificCode Specific error code for this validation
     */
    public BusinessValidationException(String message, String specificCode) {
        super(
            message, 
            String.format("%s_%s", ERROR_CODE_PREFIX, specificCode),
            HttpStatus.BAD_REQUEST.value()
        );
    }
}
