package com.turkcell.spring_cqrs.infrastructure.persistance.postgresql;

import com.turkcell.spring_cqrs.domain.entity.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLog, UUID> {

}
