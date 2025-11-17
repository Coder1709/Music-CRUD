package com.arpit.MusicApp.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a requested resource is not found.
 * Follows Single Responsibility Principle - handles only resource not found scenarios.
 * 
 * Examples: Song not found, User not found, Playlist not found
 */
public class ResourceNotFoundException extends MusicAppException {
    
    private static final String ERROR_CODE_PREFIX = "RESOURCE_NOT_FOUND";
    
    /**
     * Constructor with resource type and ID
     * 
     * @param resourceName The name of the resource (e.g., "Song", "User", "Playlist")
     * @param fieldName The field used for lookup (e.g., "id", "username")
     * @param fieldValue The value that was not found
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(
            String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue),
            String.format("%s_%s", ERROR_CODE_PREFIX, resourceName.toUpperCase()),
            HttpStatus.NOT_FOUND.value()
        );
    }
    
    /**
     * Constructor with custom message
     * 
     * @param message Custom error message
     */
    public ResourceNotFoundException(String message) {
        super(message, ERROR_CODE_PREFIX, HttpStatus.NOT_FOUND.value());
    }
}
