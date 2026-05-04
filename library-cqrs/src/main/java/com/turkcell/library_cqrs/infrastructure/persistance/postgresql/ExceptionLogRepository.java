package com.turkcell.library_cqrs.infrastructure.persistance.postgresql;

import com.turkcell.library_cqrs.domain.entity.ExceptionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Repository for ExceptionLog entity.
 * Provides database operations for exception logging and retrieval.
 */
@Repository
public interface ExceptionLogRepository extends JpaRepository<ExceptionLog, UUID> {
    
    /**
     * Find all exception logs for a specific exception type
     */
    Page<ExceptionLog> findByExceptionType(String exceptionType, Pageable pageable);
    
    /**
     * Find all exception logs created within a date range
     */
    Page<ExceptionLog> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Find all exception logs with a specific HTTP status code
     */
    Page<ExceptionLog> findByStatusCode(Integer statusCode, Pageable pageable);
    
    /**
     * Find all exception logs for a specific user
     */
    Page<ExceptionLog> findByUserInfo(String userInfo, Pageable pageable);
    
    /**
     * Find all exception logs for a specific client IP
     */
    Page<ExceptionLog> findByClientIp(String clientIp, Pageable pageable);
    
    /**
     * Find all exception logs for a specific URI
     */
    Page<ExceptionLog> findByUri(String uri, Pageable pageable);
}
