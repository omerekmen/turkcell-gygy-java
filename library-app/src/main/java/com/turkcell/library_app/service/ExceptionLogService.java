package com.turkcell.library_app.service;

import com.turkcell.library_app.entity.ExceptionLog;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

/**
 * Service interface for exception logging operations.
 */
public interface ExceptionLogService {
    
    /**
     * Log an exception with request context
     * @param exception The exception to log
     * @param request The HTTP request context
     * @param context Additional contextual information
     * @return The created ExceptionLog entity with UUID
     */
    ExceptionLog logException(Exception exception, HttpServletRequest request, String context);
    
    /**
     * Log an exception without request context
     * @param exception The exception to log
     * @return The created ExceptionLog entity with UUID
     */
    ExceptionLog logException(Exception exception);
    
    /**
     * Retrieve exception log by ID
     * @param id The log ID
     * @return The ExceptionLog entity
     */
    ExceptionLog getById(UUID id);
}
