package com.turkcell.library_cqrs.application.features.exception.mapper;

import com.turkcell.library_cqrs.api.dto.exception.ExceptionLogResponse;
import com.turkcell.library_cqrs.domain.entity.ExceptionLog;

public final class ExceptionLogMapper {

    private ExceptionLogMapper() {
    }

    public static ExceptionLogResponse toDto(ExceptionLog entity) {
        if (entity == null) {
            return null;
        }

        return ExceptionLogResponse.builder()
            .id(entity.getId())
            .exceptionType(entity.getExceptionType())
            .message(entity.getMessage())
            .stackTrace(entity.getStackTrace())
            .method(entity.getMethod())
            .uri(entity.getUri())
            .queryParams(entity.getQueryParams())
            .requestBody(entity.getRequestBody())
            .userInfo(entity.getUserInfo())
            .clientIp(entity.getClientIp())
            .statusCode(entity.getStatusCode())
            .createdAt(entity.getCreatedAt())
            .context(entity.getContext())
            .build();
    }
}
