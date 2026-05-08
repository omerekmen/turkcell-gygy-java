package com.turkcell.spring_cqrs.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "exception_logs")
public class ExceptionLog {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "exception_type", length = 255, nullable = false)
    private String exceptionType;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "stack_trace", columnDefinition = "TEXT", nullable = false)
    private String stackTrace;

    @Column(name = "method", length = 10)
    private String method;

    @Column(name = "uri", columnDefinition = "TEXT")
    private String uri;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getExceptionType() { return exceptionType; }
    public void setExceptionType(String exceptionType) { this.exceptionType = exceptionType; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getStackTrace() { return stackTrace; }
    public void setStackTrace(String stackTrace) { this.stackTrace = stackTrace; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getUri() { return uri; }
    public void setUri(String uri) { this.uri = uri; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
