package com.turkcell.library_cqrs.application.features.exception;

import java.time.LocalDateTime;
import java.util.UUID;

public record ExceptionLogResponse(
    UUID id,
    String exceptionType,
    String message,
    String stackTrace,
    String method,
    String uri,
    String queryParams,
    String requestBody,
    String userInfo,
    String clientIp,
    Integer statusCode,
    String context,
    LocalDateTime createdAt
) {}