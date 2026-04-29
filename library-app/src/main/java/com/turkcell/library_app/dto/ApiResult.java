package com.turkcell.library_app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

/**
 * Generic API Result wrapper for all API responses.
 * Wraps both successful and failure responses with consistent structure.
 * 
 * @param <T> Type of data payload
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> {
    
    /**
     * Request success status
     */
    private boolean success;
    
    /**
     * HTTP status code
     */
    private int statusCode;
    
    /**
     * Response message
     */
    private String message;
    
    /**
     * Response data payload (nullable)
     */
    private T data;
    
    /**
     * Exception log ID for tracking errors (only populated on errors)
     */
    private String logId;
    
    /**
     * Timestamp of the response
     */
    private LocalDateTime timestamp;
    
    // Constructors
    public ApiResult() {
    }
    
    public ApiResult(boolean success, int statusCode, String message, T data, String logId, LocalDateTime timestamp) {
        this.success = success;
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.logId = logId;
        this.timestamp = timestamp;
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public String getLogId() {
        return logId;
    }
    
    public void setLogId(String logId) {
        this.logId = logId;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    // Builder Pattern Implementation
    public static <T> ApiResultBuilder<T> builder() {
        return new ApiResultBuilder<>();
    }
    
    public static class ApiResultBuilder<T> {
        private boolean success;
        private int statusCode;
        private String message;
        private T data;
        private String logId;
        private LocalDateTime timestamp;
        
        public ApiResultBuilder<T> success(boolean success) {
            this.success = success;
            return this;
        }
        
        public ApiResultBuilder<T> statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }
        
        public ApiResultBuilder<T> message(String message) {
            this.message = message;
            return this;
        }
        
        public ApiResultBuilder<T> data(T data) {
            this.data = data;
            return this;
        }
        
        public ApiResultBuilder<T> logId(String logId) {
            this.logId = logId;
            return this;
        }
        
        public ApiResultBuilder<T> timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public ApiResult<T> build() {
            return new ApiResult<>(success, statusCode, message, data, logId, timestamp);
        }
    }
    
    /**
     * Build a successful API result
     * @param statusCode HTTP status code
     * @param message Response message
     * @param data Response data
     * @return ApiResult instance
     */
    public static <T> ApiResult<T> success(int statusCode, String message, T data) {
        return ApiResult.<T>builder()
            .success(true)
            .statusCode(statusCode)
            .message(message)
            .data(data)
            .timestamp(LocalDateTime.now())
            .build();
    }
    
    /**
     * Build a successful API result with 200 status
     * @param message Response message
     * @param data Response data
     * @return ApiResult instance
     */
    public static <T> ApiResult<T> success(String message, T data) {
        return success(200, message, data);
    }
    
    /**
     * Build a successful API result without data
     * @param statusCode HTTP status code
     * @param message Response message
     * @return ApiResult instance
     */
    public static <T> ApiResult<T> success(int statusCode, String message) {
        return success(statusCode, message, null);
    }
    
    /**
     * Build a failure API result
     * @param statusCode HTTP status code
     * @param message Error message
     * @param logId Exception log ID for tracking
     * @return ApiResult instance
     */
    public static <T> ApiResult<T> error(int statusCode, String message, String logId) {
        return ApiResult.<T>builder()
            .success(false)
            .statusCode(statusCode)
            .message(message)
            .logId(logId)
            .timestamp(LocalDateTime.now())
            .build();
    }
    
    /**
     * Build a failure API result with 500 status
     * @param message Error message
     * @param logId Exception log ID for tracking
     * @return ApiResult instance
     */
    public static <T> ApiResult<T> error(String message, String logId) {
        return error(500, message, logId);
    }
}
