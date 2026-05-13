package com.turkcell.spring_cqrs.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.turkcell.spring_cqrs.infrastructure.persistance.postgresql.ExceptionLogRepository;
import com.turkcell.spring_cqrs.domain.entity.ExceptionLog;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final ExceptionLogRepository exceptionLogRepository;

    public GlobalExceptionHandler(ExceptionLogRepository exceptionLogRepository) {
        this.exceptionLogRepository = exceptionLogRepository;
    }

    private void persistException(Exception ex, WebRequest request) {
        try {
            ExceptionLog log = new ExceptionLog();
            log.setExceptionType(ex.getClass().getName());
            log.setMessage(ex.getMessage());
            StringBuilder stack = new StringBuilder();
            for (StackTraceElement ste : ex.getStackTrace()) stack.append(ste.toString()).append("\n");
            log.setStackTrace(stack.toString());
            log.setUri(request.getDescription(false));
            exceptionLogRepository.save(log);
        } catch (Exception ignore) {
            System.err.println("Failed to persist exception log: " + ignore.getMessage());
        }
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<Object> handleUnauthenticated(UnauthenticatedException ex, WebRequest request) {
        persistException(ex, request);
        String message = ex.getMessage() != null ? ex.getMessage() : "Unauthenticated";
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorized(UnauthorizedException ex, WebRequest request) {
        persistException(ex, request);
        String message = ex.getMessage() != null ? ex.getMessage() : "Forbidden";
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        persistException(ex, request);
        return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
