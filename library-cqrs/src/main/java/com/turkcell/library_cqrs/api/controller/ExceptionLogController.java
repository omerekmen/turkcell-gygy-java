package com.turkcell.library_cqrs.api.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.library_cqrs.api.dto.ApiResult;
import com.turkcell.library_cqrs.api.dto.exception.ExceptionLogResponse;
import com.turkcell.library_cqrs.application.features.exception.query.getbyid.GetExceptionLogByIdQuery;
import com.turkcell.library_cqrs.core.mediator.Mediator;

@RestController
@RequestMapping("api/v{version:1}/exception-logs")
public class ExceptionLogController {

    private final Mediator mediator;

    public ExceptionLogController(Mediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<ExceptionLogResponse>> getExceptionLog(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResult.success("Exception log retrieved successfully", mediator.send(new GetExceptionLogByIdQuery(id))));
    }
}
