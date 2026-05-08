package com.turkcell.library_cqrs.application.features.reservation;

import java.time.LocalDateTime;
import java.util.UUID;

import com.turkcell.library_cqrs.domain.enums.ReservationStatus;

public record ReservationResponse(
    UUID id,
    UUID studentId,
    UUID bookId,
    ReservationStatus status,
    Integer position,
    LocalDateTime reservedAt,
    LocalDateTime expiresAt
) {}