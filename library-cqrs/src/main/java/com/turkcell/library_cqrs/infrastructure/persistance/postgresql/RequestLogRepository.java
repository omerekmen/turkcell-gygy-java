package com.turkcell.library_cqrs.infrastructure.persistance.postgresql;

import com.turkcell.library_cqrs.domain.entity.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLog, UUID> {

}
