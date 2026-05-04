package com.turkcell.library_cqrs.application.features.reservation.command.update;

import java.time.LocalDateTime;
import java.util.UUID;

import com.turkcell.library_cqrs.api.dto.reservation.ReservationResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Command;
import com.turkcell.library_cqrs.domain.enums.ReservationStatus;

public record UpdateReservationStatusCommand(UUID id, ReservationStatus status, LocalDateTime expiresAt)
    implements Command<ReservationResponse> {
}
