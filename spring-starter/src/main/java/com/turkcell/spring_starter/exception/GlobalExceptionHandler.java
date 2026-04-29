package com.turkcell.spring_starter.exception;

import com.turkcell.spring_starter.dto.ApiResult;
import com.turkcell.spring_starter.entity.ExceptionLog;
import com.turkcell.spring_starter.service.ExceptionLogService;
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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final ExceptionLogService exceptionLogService;

    public GlobalExceptionHandler(ExceptionLogService exceptionLogService) {
        this.exceptionLogService = exceptionLogService;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));

        ExceptionLog log = exceptionLogService.logException(ex, request, "Validation Error: " + errors, HttpStatus.BAD_REQUEST.value());
        logger.warn("Validation error - {}", errors);

        return ApiResult.error(HttpStatus.BAD_REQUEST.value(), "Validation failed: " + errors, log.getId().toString());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ApiResult<?> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        int statusCode = ex.getStatusCode().value();
        String message = cleanExceptionMessage(ex);
        ExceptionLog log = exceptionLogService.logException(ex, request, message, statusCode);
        logger.warn("Response status exception - {}", message);
        return ApiResult.error(statusCode, message, log.getId().toString());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<?> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        String message = cleanExceptionMessage(ex);
        ExceptionLog log = exceptionLogService.logException(ex, request, "Unexpected Runtime Exception", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ApiResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred. " + message + " Please reference error ID: " + log.getId(), log.getId().toString());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResult<?> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        ExceptionLog log = exceptionLogService.logException(ex, request, "Resource not found: " + ex.getRequestURL(), HttpStatus.NOT_FOUND.value());
        return ApiResult.error(HttpStatus.NOT_FOUND.value(), "The requested resource was not found", log.getId().toString());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = String.format("Parameter '%s' should be of type %s", ex.getName(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "specified type");
        ExceptionLog log = exceptionLogService.logException(ex, request, message, HttpStatus.BAD_REQUEST.value());
        logger.warn("Type mismatch - {}", message);
        return ApiResult.error(HttpStatus.BAD_REQUEST.value(), message, log.getId().toString());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResult<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        String message = "The submitted data violates business rules or constraints. Please review your input and try again.";
        String rawMessage = ex.getMessage();
        if (rawMessage != null && rawMessage.contains("duplicate key")) {
            message = "A duplicate entry already exists. Please check your input.";
        }

        ExceptionLog log = exceptionLogService.logException(ex, request, "Data integrity violation", HttpStatus.CONFLICT.value());
        logger.error("Data integrity violation - {}", ex.getMessage());
        return ApiResult.error(HttpStatus.CONFLICT.value(), message, log.getId().toString());
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<?> handleDataAccessException(DataAccessException ex, HttpServletRequest request) {
        ExceptionLog log = exceptionLogService.logException(ex, request, "Database access error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        logger.error("Database error - {}", ex.getMessage());
        return ApiResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "A database error occurred. Please reference error ID: " + log.getId(), log.getId().toString());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        ExceptionLog log = exceptionLogService.logException(ex, request, "Invalid argument provided", HttpStatus.BAD_REQUEST.value());
        logger.warn("Illegal argument - {}", ex.getMessage());
        return ApiResult.error(HttpStatus.BAD_REQUEST.value(), "Invalid argument: " + cleanExceptionMessage(ex), log.getId().toString());
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<?> handleNullPointerException(NullPointerException ex, HttpServletRequest request) {
        ExceptionLog log = exceptionLogService.logException(ex, request, "Null pointer exception encountered", HttpStatus.INTERNAL_SERVER_ERROR.value());
        logger.error("NullPointerException - {}", ex.getMessage());
        return ApiResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An internal error occurred. Please reference error ID: " + log.getId(), log.getId().toString());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<?> handleGenericException(Exception ex, HttpServletRequest request) {
        String message = cleanExceptionMessage(ex);
        ExceptionLog log = exceptionLogService.logException(ex, request, "Uncaught exception", HttpStatus.INTERNAL_SERVER_ERROR.value());
        logger.error("Unhandled exception - Type: {}, Message: {}", ex.getClass().getName(), ex.getMessage());
        return ApiResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred. " + message + " Please reference error ID: " + log.getId(), log.getId().toString());
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
