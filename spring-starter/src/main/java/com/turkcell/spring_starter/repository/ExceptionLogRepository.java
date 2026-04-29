package com.turkcell.spring_starter.repository;

import com.turkcell.spring_starter.entity.ExceptionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExceptionLogRepository extends JpaRepository<ExceptionLog, UUID> {
}