package com.turkcell.library_app.controller;

import com.turkcell.library_app.dto.ApiResult;
import com.turkcell.library_app.dto.exception.ExceptionLogResponse;
import com.turkcell.library_app.entity.ExceptionLog;
import com.turkcell.library_app.service.ExceptionLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller for retrieving exception logs.
 * Allows developers to search for and examine logged exceptions using their log IDs.
 */
@RestController
@RequestMapping("/api/v{version:1}/exception-logs")
public class ExceptionLogController {
    
    private static final Logger logger = LoggerFactory.getLogger(ExceptionLogController.class);
    
    private final ExceptionLogService exceptionLogService;
    
    public ExceptionLogController(ExceptionLogService exceptionLogService) {
        this.exceptionLogService = exceptionLogService;
    }
    
    /**
     * Retrieve exception log by ID
     * @param id The exception log UUID
     * @return ApiResult containing the exception log details
     */
    @GetMapping("/{id}")
    public ApiResult<ExceptionLogResponse> getExceptionLog(@PathVariable UUID id) {
        try {
            ExceptionLog log = exceptionLogService.getById(id);
            
            if (log == null) {
                return ApiResult.error(
                    404,
                    "Exception log not found with ID: " + id,
                    null
                );
            }
            
            ExceptionLogResponse response = ExceptionLogResponse.builder()
                .id(log.getId())
                .exceptionType(log.getExceptionType())
                .message(log.getMessage())
                .stackTrace(log.getStackTrace())
                .method(log.getMethod())
                .uri(log.getUri())
                .queryParams(log.getQueryParams())
                .requestBody(log.getRequestBody())
                .userInfo(log.getUserInfo())
                .clientIp(log.getClientIp())
                .statusCode(log.getStatusCode())
                .createdAt(log.getCreatedAt())
                .context(log.getContext())
                .build();
            
            return ApiResult.success(
                "Exception log retrieved successfully",
                response
            );
            
        } catch (Exception e) {
            logger.error("Error retrieving exception log with ID: {}", id, e);
            return ApiResult.error(
                500,
                "Failed to retrieve exception log",
                null
            );
        }
    }
}
