package com.turkcell.library_cqrs.application.features.exception.mapper;

import com.turkcell.library_cqrs.application.features.exception.ExceptionLogResponse;
import com.turkcell.library_cqrs.domain.entity.ExceptionLog;

public final class ExceptionLogMapper {

    private ExceptionLogMapper() {
    }

    public static ExceptionLogResponse toDto(ExceptionLog entity) {
        if (entity == null) {
            return null;
        }

        return new ExceptionLogResponse(
            entity.getId(),
            entity.getExceptionType(),
            entity.getMessage(),
            entity.getStackTrace(),
            entity.getMethod(),
            entity.getUri(),
            entity.getQueryParams(),
            entity.getRequestBody(),
            entity.getUserInfo(),
            entity.getClientIp(),
            entity.getStatusCode(),
            entity.getContext(),
            entity.getCreatedAt()
        );
    }
}
