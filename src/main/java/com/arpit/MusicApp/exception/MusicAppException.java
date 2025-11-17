package com.arpit.MusicApp.exception;

/**
 * Base exception class for all custom application exceptions.
 * Follows Single Responsibility Principle - handles only custom exception base functionality.
 * 
 * This serves as the parent class for all domain-specific exceptions,
 * providing a common structure and making exception handling more maintainable.
 */
public abstract class MusicAppException extends RuntimeException {
    
    private final String errorCode;
    private final int httpStatus;
    
    /**
     * Constructor with message, error code, and HTTP status
     * 
     * @param message User-friendly error message
     * @param errorCode Unique error code for tracking
     * @param httpStatus HTTP status code to be returned
     */
    protected MusicAppException(String message, String errorCode, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
    
    /**
     * Constructor with message, cause, error code, and HTTP status
     * 
     * @param message User-friendly error message
     * @param cause The underlying cause of the exception
     * @param errorCode Unique error code for tracking
     * @param httpStatus HTTP status code to be returned
     */
    protected MusicAppException(String message, Throwable cause, String errorCode, int httpStatus) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public int getHttpStatus() {
        return httpStatus;
    }
}
