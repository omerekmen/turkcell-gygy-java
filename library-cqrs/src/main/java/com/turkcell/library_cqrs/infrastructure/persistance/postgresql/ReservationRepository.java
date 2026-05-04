package com.turkcell.library_cqrs.infrastructure.persistance.postgresql;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.library_cqrs.domain.entity.Reservation;
import com.turkcell.library_cqrs.domain.enums.ReservationStatus;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    Page<Reservation> findByStatus(ReservationStatus status, Pageable pageable);
    Page<Reservation> findByBookId(UUID bookId, Pageable pageable);
    Page<Reservation> findByBookIdAndStatus(UUID bookId, ReservationStatus status, Pageable pageable);
    long countByBookIdAndStatus(UUID bookId, ReservationStatus status);
}
