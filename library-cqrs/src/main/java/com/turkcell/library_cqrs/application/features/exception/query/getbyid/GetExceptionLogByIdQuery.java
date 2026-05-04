package com.turkcell.library_cqrs.application.features.exception.query.getbyid;

import java.util.UUID;

import com.turkcell.library_cqrs.api.dto.exception.ExceptionLogResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Query;

public record GetExceptionLogByIdQuery(UUID id) implements Query<ExceptionLogResponse> {
}
