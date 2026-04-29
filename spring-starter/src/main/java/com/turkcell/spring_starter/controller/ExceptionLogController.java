package com.turkcell.spring_starter.controller;

import com.turkcell.spring_starter.dto.ApiResult;
import com.turkcell.spring_starter.dto.exception.ExceptionLogResponse;
import com.turkcell.spring_starter.entity.ExceptionLog;
import com.turkcell.spring_starter.service.ExceptionLogService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v{version:1}/exception-logs")
public class ExceptionLogController {
    private final ExceptionLogService exceptionLogService;

    public ExceptionLogController(ExceptionLogService exceptionLogService) {
        this.exceptionLogService = exceptionLogService;
    }

    @GetMapping("/{id}")
    public ApiResult<ExceptionLogResponse> getById(@PathVariable UUID id) {
        ExceptionLog log = exceptionLogService.getById(id);
        if (log == null) {
            return ApiResult.error(HttpStatus.NOT_FOUND.value(), "Exception log not found", id.toString());
        }

        ExceptionLogResponse response = new ExceptionLogResponse();
        response.setId(log.getId());
        response.setExceptionType(log.getExceptionType());
        response.setMessage(log.getMessage());
        response.setStackTrace(log.getStackTrace());
        response.setMethod(log.getMethod());
        response.setUri(log.getUri());
        response.setQueryParams(log.getQueryParams());
        response.setRequestBody(log.getRequestBody());
        response.setUserInfo(log.getUserInfo());
        response.setClientIp(log.getClientIp());
        response.setStatusCode(log.getStatusCode());
        response.setContext(log.getContext());
        response.setCreatedAt(log.getCreatedAt());

        return ApiResult.success("Exception log retrieved successfully", response);
    }
}