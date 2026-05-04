package com.turkcell.library_cqrs.application.features.exception.query.getbyid;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.exception.ExceptionLogResponse;
import com.turkcell.library_cqrs.application.features.exception.mapper.ExceptionLogMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.QueryHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.ExceptionLogRepository;

@Component
public class GetExceptionLogByIdQueryHandler implements QueryHandler<GetExceptionLogByIdQuery, ExceptionLogResponse> {

    private final ExceptionLogRepository exceptionLogRepository;

    public GetExceptionLogByIdQueryHandler(ExceptionLogRepository exceptionLogRepository) {
        this.exceptionLogRepository = exceptionLogRepository;
    }

    @Override
    public ExceptionLogResponse handle(GetExceptionLogByIdQuery query) {
        return ExceptionLogMapper.toDto(exceptionLogRepository.findById(query.id())
            .orElseThrow(() -> new IllegalArgumentException("Exception log not found")));
    }
}
