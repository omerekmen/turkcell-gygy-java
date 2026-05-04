package com.turkcell.library_cqrs.infrastructure.persistance.postgresql;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.library_cqrs.domain.entity.Publisher;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, UUID> {
}
