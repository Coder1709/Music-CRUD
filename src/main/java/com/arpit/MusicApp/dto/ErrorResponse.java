package com.arpit.MusicApp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard error response structure for all API errors.
 * Follows Single Responsibility Principle - represents error response data only.
 * 
 * This provides a consistent error response format across the entire application,
 * making it easier for API clients to handle errors uniformly.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    /**
     * HTTP status code (e.g., 400, 404, 500)
     */
    private int status;
    
    /**
     * Application-specific error code for tracking and categorization
     */
    private String errorCode;
    
    /**
     * User-friendly error message
     */
    private String message;
    
    /**
     * Detailed error description (optional, for debugging)
     */
    private String details;
    
    /**
     * Timestamp when the error occurred
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    /**
     * API path where the error occurred
     */
    private String path;
    
    /**
     * List of validation errors (for input validation failures)
     */
    private List<ValidationError> validationErrors;
    
    /**
     * Constructor for simple error responses without validation errors
     * 
     * @param status HTTP status code
     * @param errorCode Application error code
     * @param message User-friendly message
     * @param details Detailed description
     * @param path Request path
     */
    public ErrorResponse(int status, String errorCode, String message, String details, String path) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.details = details;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }
    
    /**
     * Nested class representing a single field validation error
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationError {
        /**
         * Name of the field that failed validation
         */
        private String field;
        
        /**
         * Value that was rejected
         */
        private Object rejectedValue;
        
        /**
         * Validation error message
         */
        private String message;
    }
}
