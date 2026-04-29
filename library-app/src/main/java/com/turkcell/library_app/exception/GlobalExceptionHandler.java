package com.turkcell.library_app.exception;

import com.turkcell.library_app.dto.ApiResult;
import com.turkcell.library_app.entity.ExceptionLog;
import com.turkcell.library_app.service.ExceptionLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

/**
 * Global exception handler for the entire application.
 * Provides centralized exception handling, logging, and consistent API responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    private final ExceptionLogService exceptionLogService;
    
    public GlobalExceptionHandler(ExceptionLogService exceptionLogService) {
        this.exceptionLogService = exceptionLogService;
    }
    
    /**
     * Handle validation errors from @Valid annotation
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        
        String errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
        
        ExceptionLog log = exceptionLogService.logException(ex, request, 
            "Validation Error: " + errors);
        
        logger.warn("Validation error - {}", errors);
        
        return ApiResult.error(
            HttpStatus.BAD_REQUEST.value(),
            "Validation failed: " + errors,
            log.getId().toString()
        );
    }
    
    /**
     * Handle runtime exceptions
     */
    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<?> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request) {
        
        ExceptionLog log = exceptionLogService.logException(ex, request, 
            "Unexpected Runtime Exception");
        String message = cleanExceptionMessage(ex);
        
        return ApiResult.error(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred: " + message + "! Please reference error ID: " + log.getId(),
            log.getId().toString()
        );
    }
    
    /**
     * Handle resource not found exceptions (404)
     */
    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResult<?> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            HttpServletRequest request) {
        
        ExceptionLog log = exceptionLogService.logException(ex, request, 
            "Resource not found: " + ex.getRequestURL());
        
        return ApiResult.error(
            HttpStatus.NOT_FOUND.value(),
            "The requested resource was not found",
            log.getId().toString()
        );
    }
    
    /**
     * Handle type mismatch errors (e.g., invalid UUID format)
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        
        String message = String.format(
            "Parameter '%s' should be of type %s",
            ex.getName(),
            ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "specified type"
        );
        
        ExceptionLog log = exceptionLogService.logException(ex, request, message);
        
        logger.warn("Type mismatch - {}", message);
        
        return ApiResult.error(
            HttpStatus.BAD_REQUEST.value(),
            message,
            log.getId().toString()
        );
    }
    
    /**
     * Handle data integrity violations (e.g., unique constraint violations)
     */
    @ExceptionHandler({DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResult<?> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {
        
        ExceptionLog log = exceptionLogService.logException(ex, request, 
            "Data integrity violation");
        log.setStatusCode(HttpStatus.CONFLICT.value());
        
        String message = "The submitted data violates business rules or constraints. " +
                        "Please review your input and try again.";
        
        if (ex.getMessage() != null && ex.getMessage().contains("duplicate key")) {
            message = "A duplicate entry already exists. Please check your input.";
        }
        
        logger.error("Data integrity violation - {}", ex.getMessage());
        
        return ApiResult.error(
            HttpStatus.CONFLICT.value(),
            message,
            log.getId().toString()
        );
    }
    
    /**
     * Handle database access errors
     */
    @ExceptionHandler({DataAccessException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<?> handleDataAccessException(
            DataAccessException ex,
            HttpServletRequest request) {
        
        ExceptionLog log = exceptionLogService.logException(ex, request, 
            "Database access error");
        
        logger.error("Database error - {}", ex.getMessage());
        
        return ApiResult.error(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "A database error occurred. Please reference error ID: " + log.getId(),
            log.getId().toString()
        );
    }
    
    /**
     * Handle illegal argument exceptions
     */
    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {
        
        ExceptionLog log = exceptionLogService.logException(ex, request, 
            "Invalid argument provided");
        
        logger.warn("Illegal argument - {}", ex.getMessage());
        
        return ApiResult.error(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid argument: " + ex.getMessage(),
            log.getId().toString()
        );
    }
    
    /**
     * Handle null pointer exceptions
     */
    @ExceptionHandler({NullPointerException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<?> handleNullPointerException(
            NullPointerException ex,
            HttpServletRequest request) {
        
        ExceptionLog log = exceptionLogService.logException(ex, request, 
            "Null pointer exception encountered");
        
        logger.error("NullPointerException - {}", ex.getMessage());
        
        return ApiResult.error(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An internal error occurred. Please reference error ID: " + log.getId(),
            log.getId().toString()
        );
    }
    
    /**
     * Catch-all handler for any uncaught exceptions
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<?> handleGenericException(
            Exception ex,
            HttpServletRequest request) {
        
        ExceptionLog log = exceptionLogService.logException(ex, request, 
            "Uncaught exception");
        String message = cleanExceptionMessage(ex);
        
        logger.error("Unhandled exception - Type: {}, Message: {}", 
            ex.getClass().getName(), ex.getMessage());
        
        return ApiResult.error(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred. " + message + " Please reference error ID: " + log.getId(),
            log.getId().toString()
        );
    }

    private String cleanExceptionMessage(Exception ex) {
        String message = ex.getMessage();
        if (message == null || message.isBlank()) {
            message = ex.getLocalizedMessage();
        }

        if (message == null || message.isBlank()) {
            return "Unexpected server error";
        }

        int firstQuote = message.indexOf('"');
        int lastQuote = message.lastIndexOf('"');
        if (firstQuote >= 0 && lastQuote > firstQuote) {
            return message.substring(firstQuote + 1, lastQuote).trim();
        }

        int firstColon = message.indexOf(':');
        if (firstColon >= 0 && firstColon + 1 < message.length()) {
            String candidate = message.substring(firstColon + 1).trim();
            if (!candidate.isBlank()) {
                return candidate;
            }
        }

        return message.trim();
    }
}
