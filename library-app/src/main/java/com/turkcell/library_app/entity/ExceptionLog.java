package com.turkcell.library_app.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity to log all exceptions occurring in the application.
 * Provides traceability and debugging information for developers.
 */
@Entity
@Table(name = "exception_logs")
public class ExceptionLog {
    
    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;
    
    /**
     * Exception class name
     */
    @Column(name = "exception_type", nullable = false, length = 255)
    private String exceptionType;
    
    /**
     * Exception message
     */
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;
    
    /**
     * Full stack trace of the exception
     */
    @Column(name = "stack_trace", columnDefinition = "TEXT", nullable = false)
    private String stackTrace;
    
    /**
     * HTTP request method (GET, POST, PUT, DELETE, etc.)
     */
    @Column(name = "method", length = 10)
    private String method;
    
    /**
     * Request URI path
     */
    @Column(name = "uri", columnDefinition = "TEXT")
    private String uri;
    
    /**
     * Query parameters from the request
     */
    @Column(name = "query_params", columnDefinition = "TEXT")
    private String queryParams;
    
    /**
     * Request body (as JSON string)
     */
    @Column(name = "request_body", columnDefinition = "TEXT")
    private String requestBody;
    
    /**
     * User information (username or user ID if available)
     */
    @Column(name = "user_info", length = 255)
    private String userInfo;
    
    /**
     * IP address of the client
     */
    @Column(name = "client_ip", length = 45)
    private String clientIp;
    
    /**
     * HTTP status code returned
     */
    @Column(name = "status_code")
    private Integer statusCode;
    
    /**
     * Timestamp when the exception occurred
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * Additional contextual information
     */
    @Column(name = "context", columnDefinition = "TEXT")
    private String context;
    
    // Constructors
    public ExceptionLog() {
    }
    
    public ExceptionLog(UUID id, String exceptionType, String message, String stackTrace, 
                       String method, String uri, String queryParams, String requestBody, 
                       String userInfo, String clientIp, Integer statusCode, LocalDateTime createdAt, String context) {
        this.id = id;
        this.exceptionType = exceptionType;
        this.message = message;
        this.stackTrace = stackTrace;
        this.method = method;
        this.uri = uri;
        this.queryParams = queryParams;
        this.requestBody = requestBody;
        this.userInfo = userInfo;
        this.clientIp = clientIp;
        this.statusCode = statusCode;
        this.createdAt = createdAt;
        this.context = context;
    }
    
    // Builder Pattern Implementation
    public static ExceptionLogBuilder builder() {
        return new ExceptionLogBuilder();
    }
    
    public static class ExceptionLogBuilder {
        private UUID id;
        private String exceptionType;
        private String message;
        private String stackTrace;
        private String method;
        private String uri;
        private String queryParams;
        private String requestBody;
        private String userInfo;
        private String clientIp;
        private Integer statusCode;
        private LocalDateTime createdAt;
        private String context;
        
        public ExceptionLogBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        
        public ExceptionLogBuilder exceptionType(String exceptionType) {
            this.exceptionType = exceptionType;
            return this;
        }
        
        public ExceptionLogBuilder message(String message) {
            this.message = message;
            return this;
        }
        
        public ExceptionLogBuilder stackTrace(String stackTrace) {
            this.stackTrace = stackTrace;
            return this;
        }
        
        public ExceptionLogBuilder method(String method) {
            this.method = method;
            return this;
        }
        
        public ExceptionLogBuilder uri(String uri) {
            this.uri = uri;
            return this;
        }
        
        public ExceptionLogBuilder queryParams(String queryParams) {
            this.queryParams = queryParams;
            return this;
        }
        
        public ExceptionLogBuilder requestBody(String requestBody) {
            this.requestBody = requestBody;
            return this;
        }
        
        public ExceptionLogBuilder userInfo(String userInfo) {
            this.userInfo = userInfo;
            return this;
        }
        
        public ExceptionLogBuilder clientIp(String clientIp) {
            this.clientIp = clientIp;
            return this;
        }
        
        public ExceptionLogBuilder statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }
        
        public ExceptionLogBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public ExceptionLogBuilder context(String context) {
            this.context = context;
            return this;
        }
        
        public ExceptionLog build() {
            return new ExceptionLog(id, exceptionType, message, stackTrace, method, uri, 
                                   queryParams, requestBody, userInfo, clientIp, statusCode, createdAt, context);
        }
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getExceptionType() {
        return exceptionType;
    }
    
    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getStackTrace() {
        return stackTrace;
    }
    
    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    
    public String getUri() {
        return uri;
    }
    
    public void setUri(String uri) {
        this.uri = uri;
    }
    
    public String getQueryParams() {
        return queryParams;
    }
    
    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }
    
    public String getRequestBody() {
        return requestBody;
    }
    
    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }
    
    public String getUserInfo() {
        return userInfo;
    }
    
    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }
    
    public String getClientIp() {
        return clientIp;
    }
    
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
    
    public Integer getStatusCode() {
        return statusCode;
    }
    
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getContext() {
        return context;
    }
    
    public void setContext(String context) {
        this.context = context;
    }
}
