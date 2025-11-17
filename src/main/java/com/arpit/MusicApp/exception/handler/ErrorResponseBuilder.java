package com.arpit.MusicApp.exception.handler;

import com.arpit.MusicApp.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Builder class for creating standardized error responses.
 * Follows Single Responsibility Principle - only builds ErrorResponse objects.
 * Follows Builder Pattern for flexible and readable error response construction.
 * 
 * This class centralizes error response creation logic, making it easier to
 * maintain consistent error formatting across the application.
 */
@Component
public class ErrorResponseBuilder {
    
    /**
     * Builds a standard error response without validation errors
     * 
     * @param status HTTP status code
     * @param errorCode Application error code
     * @param message User-friendly error message
     * @param details Detailed error information
     * @param request HTTP servlet request for path extraction
     * @return Constructed ErrorResponse object
     */
    public ErrorResponse buildErrorResponse(
            int status, 
            String errorCode, 
            String message, 
            String details, 
            HttpServletRequest request) {
        
        ErrorResponse response = new ErrorResponse();
        response.setStatus(status);
        response.setErrorCode(errorCode);
        response.setMessage(message);
        response.setDetails(details);
        response.setTimestamp(LocalDateTime.now());
        response.setPath(request != null ? request.getRequestURI() : "unknown");
        
        return response;
    }
    
    /**
     * Builds an error response with validation errors from Spring Validation
     * 
     * @param status HTTP status code
     * @param errorCode Application error code
     * @param message User-friendly error message
     * @param fieldErrors List of field validation errors
     * @param request HTTP servlet request for path extraction
     * @return Constructed ErrorResponse object with validation errors
     */
    public ErrorResponse buildValidationErrorResponse(
            int status,
            String errorCode,
            String message,
            List<FieldError> fieldErrors,
            HttpServletRequest request) {
        
        ErrorResponse response = buildErrorResponse(status, errorCode, message, 
                "Validation failed for one or more fields", request);
        
        // Convert Spring FieldError objects to our ValidationError DTOs
        List<ErrorResponse.ValidationError> validationErrors = fieldErrors.stream()
                .map(error -> new ErrorResponse.ValidationError(
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());
        
        response.setValidationErrors(validationErrors);
        
        return response;
    }
    
    /**
     * Builds a simple error response with just message (for quick responses)
     * 
     * @param status HTTP status code
     * @param message User-friendly error message
     * @param request HTTP servlet request for path extraction
     * @return Constructed ErrorResponse object
     */
    public ErrorResponse buildSimpleErrorResponse(
            int status,
            String message,
            HttpServletRequest request) {
        
        return buildErrorResponse(
                status,
                "ERROR_" + status,
                message,
                null,
                request
        );
    }
}
