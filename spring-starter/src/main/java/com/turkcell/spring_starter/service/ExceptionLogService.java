package com.turkcell.spring_starter.service;

import com.turkcell.spring_starter.entity.ExceptionLog;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface ExceptionLogService {
    ExceptionLog logException(Exception exception, HttpServletRequest request, String context, int statusCode);

    ExceptionLog logException(Exception exception, HttpServletRequest request, String context);

    ExceptionLog logException(Exception exception);

    ExceptionLog getById(UUID id);
}