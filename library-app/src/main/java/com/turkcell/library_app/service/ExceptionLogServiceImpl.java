package com.turkcell.library_app.service;

import com.turkcell.library_app.entity.ExceptionLog;
import com.turkcell.library_app.repository.ExceptionLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

/**
 * Implementation of ExceptionLogService.
 * Handles logging exceptions to the database with request context.
 */
@Service
public class ExceptionLogServiceImpl implements ExceptionLogService {
    
    private static final Logger logger = LoggerFactory.getLogger(ExceptionLogServiceImpl.class);
    
    private final ExceptionLogRepository exceptionLogRepository;
    
    public ExceptionLogServiceImpl(ExceptionLogRepository exceptionLogRepository) {
        this.exceptionLogRepository = exceptionLogRepository;
    }
    
    @Override
    @Transactional
    public ExceptionLog logException(Exception exception, HttpServletRequest request, String context) {
        try {
            ExceptionLog log = ExceptionLog.builder()
                .exceptionType(exception.getClass().getName())
                .message(exception.getMessage())
                .stackTrace(getStackTrace(exception))
                .method(request.getMethod())
                .uri(request.getRequestURI())
                .queryParams(request.getQueryString())
                .userInfo(getUserInfo(request))
                .clientIp(getClientIp(request))
                .context(context)
                .statusCode(500)
                .build();
            
            ExceptionLog savedLog = exceptionLogRepository.save(log);
            log.setId(savedLog.getId());
            
            logger.warn("Exception logged with ID: {}", savedLog.getId(), exception);
            
            return savedLog;
        } catch (Exception e) {
            logger.error("Failed to log exception", e);
            throw new RuntimeException("Failed to persist exception log", e);
        }
    }
    
    @Override
    @Transactional
    public ExceptionLog logException(Exception exception) {
        try {
            ExceptionLog log = ExceptionLog.builder()
                .exceptionType(exception.getClass().getName())
                .message(exception.getMessage())
                .stackTrace(getStackTrace(exception))
                .statusCode(500)
                .build();
            
            ExceptionLog savedLog = exceptionLogRepository.save(log);
            log.setId(savedLog.getId());
            
            logger.warn("Exception logged with ID: {}", savedLog.getId(), exception);
            
            return savedLog;
        } catch (Exception e) {
            logger.error("Failed to log exception", e);
            throw new RuntimeException("Failed to persist exception log", e);
        }
    }
    
    @Override
    public ExceptionLog getById(UUID id) {
        return exceptionLogRepository.findById(id)
            .orElse(null);
    }
    
    /**
     * Convert exception stack trace to string
     */
    private String getStackTrace(Exception exception) {
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
    
    /**
     * Extract user information from request
     */
    private String getUserInfo(HttpServletRequest request) {
        String user = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "ANONYMOUS";
        return user;
    }
    
    /**
     * Extract client IP from request (handles X-Forwarded-For header for proxied requests)
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
