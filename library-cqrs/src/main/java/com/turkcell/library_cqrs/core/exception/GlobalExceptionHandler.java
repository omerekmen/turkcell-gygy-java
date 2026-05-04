package com.turkcell.library_cqrs.core.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.turkcell.library_cqrs.api.dto.ApiResult;
import com.turkcell.library_cqrs.domain.entity.ExceptionLog;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.ExceptionLogRepository;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final ExceptionLogRepository exceptionLogRepository;

    public GlobalExceptionHandler(ExceptionLogRepository exceptionLogRepository) {
        this.exceptionLogRepository = exceptionLogRepository;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult<?> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));

        ExceptionLog log = saveLog(ex, request, "Validation error: " + errors, HttpStatus.BAD_REQUEST.value());
        logger.warn("Validation error on {} - {}", request.getRequestURI(), errors);
        return ApiResult.error(HttpStatus.BAD_REQUEST.value(), "Validation failed: " + errors, log.getId().toString());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResult<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = String.format(
            "Parameter '%s' should be of type %s",
            ex.getName(),
            ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "specified type"
        );
        ExceptionLog log = saveLog(ex, request, message, HttpStatus.BAD_REQUEST.value());
        logger.warn("Type mismatch on {} - {}", request.getRequestURI(), message);
        return ApiResult.error(HttpStatus.BAD_REQUEST.value(), message, log.getId().toString());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiResult<?> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        String message = ex.getMessage() != null && ex.getMessage().contains("duplicate")
            ? "A duplicate entry already exists. Please check your input."
            : "The submitted data violates business rules or constraints. Please review your input and try again.";

        ExceptionLog log = saveLog(ex, request, message, HttpStatus.CONFLICT.value());
        logger.error("Data integrity violation on {} - {}", request.getRequestURI(), ex.getMessage());
        return ApiResult.error(HttpStatus.CONFLICT.value(), message, log.getId().toString());
    }

    @ExceptionHandler(DataAccessException.class)
    public ApiResult<?> handleDataAccess(DataAccessException ex, HttpServletRequest request) {
        ExceptionLog log = saveLog(ex, request, "Database error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        logger.error("Database error on {} - {}", request.getRequestURI(), ex.getMessage());
        return ApiResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "A database error occurred. Please reference error ID: " + log.getId(), log.getId().toString());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResult<?> handleNotFound(NoHandlerFoundException ex, HttpServletRequest request) {
        ExceptionLog log = saveLog(ex, request, "Route not found", HttpStatus.NOT_FOUND.value());
        logger.warn("Route not found {} {}", request.getMethod(), request.getRequestURI());
        return ApiResult.error(HttpStatus.NOT_FOUND.value(), "The requested resource was not found", log.getId().toString());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResult<?> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        ExceptionLog log = saveLog(ex, request, "Invalid argument provided", HttpStatus.BAD_REQUEST.value());
        logger.warn("Illegal argument on {} - {}", request.getRequestURI(), ex.getMessage());
        return ApiResult.error(HttpStatus.BAD_REQUEST.value(), "Invalid argument: " + ex.getMessage(), log.getId().toString());
    }

    @ExceptionHandler(NullPointerException.class)
    public ApiResult<?> handleNullPointer(NullPointerException ex, HttpServletRequest request) {
        ExceptionLog log = saveLog(ex, request, "Null pointer exception encountered", HttpStatus.INTERNAL_SERVER_ERROR.value());
        logger.error("Null pointer on {} - {}", request.getRequestURI(), ex.getMessage());
        return ApiResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An internal error occurred. Please reference error ID: " + log.getId(), log.getId().toString());
    }

    @ExceptionHandler(RuntimeException.class)
    public ApiResult<?> handleRuntime(RuntimeException ex, HttpServletRequest request) {
        ExceptionLog log = saveLog(ex, request, "Unexpected runtime exception", HttpStatus.INTERNAL_SERVER_ERROR.value());
        logger.error("Runtime exception on {} - {}", request.getRequestURI(), ex.getMessage(), ex);
        return ApiResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred. Please reference error ID: " + log.getId(), log.getId().toString());
    }

    @ExceptionHandler(Exception.class)
    public ApiResult<?> handleGeneric(Exception ex, HttpServletRequest request) {
        ExceptionLog log = saveLog(ex, request, "Unhandled exception", HttpStatus.INTERNAL_SERVER_ERROR.value());
        logger.error("Unhandled exception on {} - {}", request.getRequestURI(), ex.getMessage(), ex);
        return ApiResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred. Please reference error ID: " + log.getId(), log.getId().toString());
    }

    private ExceptionLog saveLog(Exception ex, HttpServletRequest request, String context, int statusCode) {
        var log = new ExceptionLog();
        log.setExceptionType(ex.getClass().getName());
        log.setMessage(ex.getMessage());
        log.setStackTrace(stackTrace(ex));
        log.setMethod(request.getMethod());
        log.setUri(request.getRequestURI());
        log.setQueryParams(request.getQueryString());
        log.setRequestBody(null);
        log.setUserInfo(request.getRemoteUser());
        log.setClientIp(request.getRemoteAddr());
        log.setStatusCode(statusCode);
        log.setContext(context);
        return exceptionLogRepository.save(log);
    }

    private String stackTrace(Exception ex) {
        var writer = new StringWriter();
        ex.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
