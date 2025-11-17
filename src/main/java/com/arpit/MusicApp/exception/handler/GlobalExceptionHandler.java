package com.arpit.MusicApp.exception.handler;

import com.arpit.MusicApp.dto.ErrorResponse;
import com.arpit.MusicApp.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

/**
 * Global Exception Handler for the Music Streaming Application.
 * 
 * SOLID Principles Applied:
 * - Single Responsibility: Handles only exception-to-response conversion
 * - Open/Closed: Open for extension (new exception types), closed for modification
 * - Liskov Substitution: All MusicAppException subtypes handled uniformly
 * - Interface Segregation: Uses specific exception types rather than generic Exception
 * - Dependency Inversion: Depends on ErrorResponseBuilder abstraction
 * 
 * This class intercepts all exceptions thrown by controllers and converts them
 * into standardized ErrorResponse objects with appropriate HTTP status codes.
 * All exceptions are logged with contextual information for debugging and monitoring.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    // Logger instance for this class - using Log4j2
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);
    
    // Logger specifically for exception details
    private static final Logger exceptionLogger = LogManager.getLogger("com.arpit.MusicApp.exception");
    
    @Autowired
    private ErrorResponseBuilder errorResponseBuilder;
    
    /**
     * Handles ResourceNotFoundException - when a requested resource is not found.
     * Returns 404 Not Found status.
     * 
     * @param ex The exception thrown
     * @param request The HTTP request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {
        
        // Log the exception at WARN level (not critical, but important)
        logger.warn("Resource not found: {} | Path: {}", ex.getMessage(), request.getRequestURI());
        exceptionLogger.warn("ResourceNotFoundException details", ex);
        
        ErrorResponse errorResponse = errorResponseBuilder.buildErrorResponse(
                ex.getHttpStatus(),
                ex.getErrorCode(),
                ex.getMessage(),
                "The requested resource could not be found in the system",
                request
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getHttpStatus()));
    }
    
    /**
     * Handles UnauthorizedAccessException - when user lacks permission.
     * Returns 403 Forbidden status.
     * 
     * @param ex The exception thrown
     * @param request The HTTP request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccessException(
            UnauthorizedAccessException ex,
            HttpServletRequest request) {
        
        // Log security-related issues at WARN level
        logger.warn("Unauthorized access attempt: {} | Path: {} | User: {}", 
                ex.getMessage(), 
                request.getRequestURI(),
                request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "Anonymous");
        exceptionLogger.warn("UnauthorizedAccessException details", ex);
        
        ErrorResponse errorResponse = errorResponseBuilder.buildErrorResponse(
                ex.getHttpStatus(),
                ex.getErrorCode(),
                ex.getMessage(),
                "You do not have sufficient permissions to access this resource",
                request
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getHttpStatus()));
    }
    
    /**
     * Handles BusinessValidationException - when business rules are violated.
     * Returns 400 Bad Request status.
     * 
     * @param ex The exception thrown
     * @param request The HTTP request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessValidationException(
            BusinessValidationException ex,
            HttpServletRequest request) {
        
        // Log business validation failures at INFO level (expected behavior)
        logger.info("Business validation failed: {} | Path: {}", ex.getMessage(), request.getRequestURI());
        exceptionLogger.info("BusinessValidationException details", ex);
        
        ErrorResponse errorResponse = errorResponseBuilder.buildErrorResponse(
                ex.getHttpStatus(),
                ex.getErrorCode(),
                ex.getMessage(),
                "Business validation rules were not met",
                request
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getHttpStatus()));
    }
    
    /**
     * Handles custom AuthenticationException - when authentication fails.
     * Returns 401 Unauthorized status.
     * 
     * @param ex The exception thrown
     * @param request The HTTP request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(com.arpit.MusicApp.exception.AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleCustomAuthenticationException(
            com.arpit.MusicApp.exception.AuthenticationException ex,
            HttpServletRequest request) {
        
        // Log authentication failures at WARN level (security concern)
        logger.warn("Authentication failed: {} | Path: {}", ex.getMessage(), request.getRequestURI());
        exceptionLogger.warn("AuthenticationException details", ex);
        
        ErrorResponse errorResponse = errorResponseBuilder.buildErrorResponse(
                ex.getHttpStatus(),
                ex.getErrorCode(),
                ex.getMessage(),
                "Authentication credentials are invalid or missing",
                request
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getHttpStatus()));
    }
    
    /**
     * Handles all custom MusicAppException subtypes not caught by specific handlers.
     * This provides a fallback for any new custom exceptions added in the future.
     * 
     * @param ex The exception thrown
     * @param request The HTTP request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(MusicAppException.class)
    public ResponseEntity<ErrorResponse> handleMusicAppException(
            MusicAppException ex,
            HttpServletRequest request) {
        
        // Log at ERROR level as this catches unexpected custom exceptions
        logger.error("MusicApp exception occurred: {} | Path: {}", ex.getMessage(), request.getRequestURI());
        exceptionLogger.error("MusicAppException details", ex);
        
        ErrorResponse errorResponse = errorResponseBuilder.buildErrorResponse(
                ex.getHttpStatus(),
                ex.getErrorCode(),
                ex.getMessage(),
                "An application error occurred while processing your request",
                request
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getHttpStatus()));
    }
    
    /**
     * Handles Spring Security AuthenticationException.
     * Returns 401 Unauthorized status.
     * 
     * @param ex The exception thrown
     * @param request The HTTP request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex,
            HttpServletRequest request) {
        
        // Log authentication failures
        logger.warn("Spring Security authentication failed: {} | Path: {}", ex.getMessage(), request.getRequestURI());
        exceptionLogger.warn("Spring AuthenticationException details", ex);
        
        ErrorResponse errorResponse = errorResponseBuilder.buildErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "AUTHENTICATION_FAILED",
                "Authentication failed. Please check your credentials.",
                ex.getMessage(),
                request
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    
    /**
     * Handles Spring Security AccessDeniedException.
     * Returns 403 Forbidden status.
     * 
     * @param ex The exception thrown
     * @param request The HTTP request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex,
            HttpServletRequest request) {
        
        // Log access denied attempts
        logger.warn("Access denied: {} | Path: {} | User: {}", 
                ex.getMessage(), 
                request.getRequestURI(),
                request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "Anonymous");
        exceptionLogger.warn("AccessDeniedException details", ex);
        
        ErrorResponse errorResponse = errorResponseBuilder.buildErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "ACCESS_DENIED",
                "Access is denied. You don't have permission to access this resource.",
                ex.getMessage(),
                request
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    
    /**
     * Handles MethodArgumentNotValidException - when request body validation fails.
     * Returns 400 Bad Request status with detailed validation errors.
     * 
     * @param ex The exception thrown
     * @param request The HTTP request
     * @return ResponseEntity with validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        
        // Extract field errors from the exception
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        
        // Log validation failures at INFO level (expected user input errors)
        logger.info("Validation failed for {} field(s) | Path: {}", fieldErrors.size(), request.getRequestURI());
        fieldErrors.forEach(error -> 
            logger.debug("Field: {} | Rejected value: {} | Message: {}", 
                    error.getField(), error.getRejectedValue(), error.getDefaultMessage())
        );
        
        ErrorResponse errorResponse = errorResponseBuilder.buildValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_FAILED",
                "Input validation failed",
                fieldErrors,
                request
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handles MissingServletRequestParameterException - when required parameter is missing.
     * Returns 400 Bad Request status.
     * 
     * @param ex The exception thrown
     * @param request The HTTP request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex,
            HttpServletRequest request) {
        
        logger.info("Missing request parameter: {} | Path: {}", ex.getParameterName(), request.getRequestURI());
        
        String message = String.format("Required parameter '%s' is missing", ex.getParameterName());
        ErrorResponse errorResponse = errorResponseBuilder.buildErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "MISSING_PARAMETER",
                message,
                ex.getMessage(),
                request
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handles MethodArgumentTypeMismatchException - when parameter type conversion fails.
     * Returns 400 Bad Request status.
     * 
     * @param ex The exception thrown
     * @param request The HTTP request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        
        logger.info("Type mismatch for parameter: {} | Path: {}", ex.getName(), request.getRequestURI());
        
        String message = String.format("Parameter '%s' should be of type %s", 
                ex.getName(), 
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
        
        ErrorResponse errorResponse = errorResponseBuilder.buildErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "TYPE_MISMATCH",
                message,
                ex.getMessage(),
                request
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handles HttpMessageNotReadableException - when request body cannot be parsed.
     * Returns 400 Bad Request status.
     * 
     * @param ex The exception thrown
     * @param request The HTTP request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        
        logger.warn("Malformed JSON request | Path: {}", request.getRequestURI());
        exceptionLogger.warn("HttpMessageNotReadableException details", ex);
        
        ErrorResponse errorResponse = errorResponseBuilder.buildErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "MALFORMED_JSON",
                "Request body is malformed or invalid",
                "Please check your JSON syntax and data types",
                request
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handles HttpRequestMethodNotSupportedException - when HTTP method is not supported.
     * Returns 405 Method Not Allowed status.
     * 
     * @param ex The exception thrown
     * @param request The HTTP request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {
        
        logger.info("HTTP method not supported: {} | Path: {}", ex.getMethod(), request.getRequestURI());
        
        String message = String.format("HTTP method '%s' is not supported for this endpoint", ex.getMethod());
        ErrorResponse errorResponse = errorResponseBuilder.buildErrorResponse(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "METHOD_NOT_ALLOWED",
                message,
                String.format("Supported methods: %s", ex.getSupportedHttpMethods()),
                request
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }
    
    /**
     * Handles HttpMediaTypeNotSupportedException - when media type is not supported.
     * Returns 415 Unsupported Media Type status.
     * 
     * @param ex The exception thrown
     * @param request The HTTP request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex,
            HttpServletRequest request) {
        
        logger.info("Unsupported media type: {} | Path: {}", ex.getContentType(), request.getRequestURI());
        
        ErrorResponse errorResponse = errorResponseBuilder.buildErrorResponse(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                "UNSUPPORTED_MEDIA_TYPE",
                "Content-Type is not supported",
                String.format("Supported media types: %s", ex.getSupportedMediaTypes()),
                request
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
    
    /**
     * Handles NoHandlerFoundException - when no handler is found for the request.
     * Returns 404 Not Found status.
     * 
     * @param ex The exception thrown
     * @param request The HTTP request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            HttpServletRequest request) {
        
        logger.info("No handler found for: {} {} | Path: {}", 
                ex.getHttpMethod(), ex.getRequestURL(), request.getRequestURI());
        
        ErrorResponse errorResponse = errorResponseBuilder.buildErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "ENDPOINT_NOT_FOUND",
                "The requested endpoint does not exist",
                String.format("No handler found for %s %s", ex.getHttpMethod(), ex.getRequestURL()),
                request
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Handles DataIntegrityViolationException - database constraint violations.
     * Returns 409 Conflict status.
     * 
     * @param ex The exception thrown
     * @param request The HTTP request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {
        
        logger.error("Data integrity violation | Path: {}", request.getRequestURI());
        exceptionLogger.error("DataIntegrityViolationException details", ex);
        
        // Determine specific constraint violation message
        String message = "Database constraint violation";
        if (ex.getMessage().contains("unique") || ex.getMessage().contains("duplicate")) {
            message = "A record with this information already exists";
        } else if (ex.getMessage().contains("foreign key")) {
            message = "Cannot perform operation due to related data constraints";
        }
        
        ErrorResponse errorResponse = errorResponseBuilder.buildErrorResponse(
                HttpStatus.CONFLICT.value(),
                "DATA_INTEGRITY_VIOLATION",
                message,
                "Please check your data and try again",
                request
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    
    /**
     * Handles all other unexpected exceptions.
     * Returns 500 Internal Server Error status.
     * This is the catch-all handler for any exceptions not specifically handled above.
     * 
     * @param ex The exception thrown
     * @param request The HTTP request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {
        
        // Log unexpected errors at ERROR level with full stack trace
        logger.error("Unexpected error occurred | Path: {} | Exception: {}", 
                request.getRequestURI(), ex.getClass().getName());
        exceptionLogger.error("Unexpected exception details", ex);
        
        ErrorResponse errorResponse = errorResponseBuilder.buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred",
                "Please contact support if the problem persists",
                request
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
