package com.turkcell.library_cqrs.api.dto.exception;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for retrieving exception log details.
 * Used by developers to search and view logged exceptions.
 */
public class ExceptionLogResponse {
    
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
    
    // Constructors
    public ExceptionLogResponse() {
    }
    
    public ExceptionLogResponse(UUID id, String exceptionType, String message, String stackTrace,
                                String method, String uri, String queryParams, String requestBody,
                                String userInfo, String clientIp, Integer statusCode, LocalDateTime createdAt,
                                String context) {
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
    public static ExceptionLogResponseBuilder builder() {
        return new ExceptionLogResponseBuilder();
    }
    
    public static class ExceptionLogResponseBuilder {
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
        
        public ExceptionLogResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        
        public ExceptionLogResponseBuilder exceptionType(String exceptionType) {
            this.exceptionType = exceptionType;
            return this;
        }
        
        public ExceptionLogResponseBuilder message(String message) {
            this.message = message;
            return this;
        }
        
        public ExceptionLogResponseBuilder stackTrace(String stackTrace) {
            this.stackTrace = stackTrace;
            return this;
        }
        
        public ExceptionLogResponseBuilder method(String method) {
            this.method = method;
            return this;
        }
        
        public ExceptionLogResponseBuilder uri(String uri) {
            this.uri = uri;
            return this;
        }
        
        public ExceptionLogResponseBuilder queryParams(String queryParams) {
            this.queryParams = queryParams;
            return this;
        }
        
        public ExceptionLogResponseBuilder requestBody(String requestBody) {
            this.requestBody = requestBody;
            return this;
        }
        
        public ExceptionLogResponseBuilder userInfo(String userInfo) {
            this.userInfo = userInfo;
            return this;
        }
        
        public ExceptionLogResponseBuilder clientIp(String clientIp) {
            this.clientIp = clientIp;
            return this;
        }
        
        public ExceptionLogResponseBuilder statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }
        
        public ExceptionLogResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public ExceptionLogResponseBuilder context(String context) {
            this.context = context;
            return this;
        }
        
        public ExceptionLogResponse build() {
            return new ExceptionLogResponse(id, exceptionType, message, stackTrace, method, uri,
                                           queryParams, requestBody, userInfo, clientIp, statusCode,
                                           createdAt, context);
        }
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
