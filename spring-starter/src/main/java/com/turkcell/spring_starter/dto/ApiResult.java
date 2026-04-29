package com.turkcell.spring_starter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> {
    private boolean success;
    private int statusCode;
    private String message;
    private T data;
    private String logId;
    private LocalDateTime timestamp;

    public ApiResult() {
    }

    public ApiResult(boolean success, int statusCode, String message, T data, String logId, LocalDateTime timestamp) {
        this.success = success;
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.logId = logId;
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static <T> ApiResult<T> success(int statusCode, String message, T data) {
        return new ApiResult<>(true, statusCode, message, data, null, LocalDateTime.now());
    }

    public static <T> ApiResult<T> success(String message, T data) {
        return success(200, message, data);
    }

    public static <T> ApiResult<T> success(int statusCode, String message) {
        return success(statusCode, message, null);
    }

    public static <T> ApiResult<T> error(int statusCode, String message, String logId) {
        return new ApiResult<>(false, statusCode, message, null, logId, LocalDateTime.now());
    }

    public static <T> ApiResult<T> error(String message, String logId) {
        return error(500, message, logId);
    }
}