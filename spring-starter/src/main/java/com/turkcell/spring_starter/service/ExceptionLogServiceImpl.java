package com.turkcell.spring_starter.service;

import com.turkcell.spring_starter.entity.ExceptionLog;
import com.turkcell.spring_starter.repository.ExceptionLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

@Service
public class ExceptionLogServiceImpl implements ExceptionLogService {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionLogServiceImpl.class);

    private final ExceptionLogRepository exceptionLogRepository;

    public ExceptionLogServiceImpl(ExceptionLogRepository exceptionLogRepository) {
        this.exceptionLogRepository = exceptionLogRepository;
    }

    @Override
    public ExceptionLog logException(Exception exception, HttpServletRequest request, String context, int statusCode) {
        ExceptionLog exceptionLog = createLog(exception, request, context, statusCode);
        ExceptionLog savedLog = exceptionLogRepository.save(exceptionLog);
        logger.warn("Exception logged with ID: {}", savedLog.getId(), exception);
        return savedLog;
    }

    @Override
    public ExceptionLog logException(Exception exception, HttpServletRequest request, String context) {
        return logException(exception, request, context, 500);
    }

    @Override
    public ExceptionLog logException(Exception exception) {
        ExceptionLog exceptionLog = createLog(exception, null, null, 500);
        ExceptionLog savedLog = exceptionLogRepository.save(exceptionLog);
        logger.warn("Exception logged with ID: {}", savedLog.getId(), exception);
        return savedLog;
    }

    @Override
    public ExceptionLog getById(UUID id) {
        return exceptionLogRepository.findById(id).orElse(null);
    }

    private ExceptionLog createLog(Exception exception, HttpServletRequest request, String context, int statusCode) {
        ExceptionLog exceptionLog = new ExceptionLog();
        exceptionLog.setExceptionType(exception.getClass().getName());
        exceptionLog.setMessage(exception.getMessage());
        exceptionLog.setStackTrace(getStackTrace(exception));
        exceptionLog.setStatusCode(statusCode);
        exceptionLog.setContext(context);

        if (request != null) {
            exceptionLog.setMethod(request.getMethod());
            exceptionLog.setUri(request.getRequestURI());
            exceptionLog.setQueryParams(request.getQueryString());
            exceptionLog.setUserInfo(getUserInfo(request));
            exceptionLog.setClientIp(getClientIp(request));
        }

        return exceptionLog;
    }

    private String getStackTrace(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    private String getUserInfo(HttpServletRequest request) {
        return request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "ANONYMOUS";
    }

    private String getClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp;
        }

        return request.getRemoteAddr();
    }
}