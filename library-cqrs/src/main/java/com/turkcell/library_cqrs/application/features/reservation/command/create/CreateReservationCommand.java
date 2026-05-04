package com.turkcell.library_cqrs.application.features.reservation.command.create;

import java.time.LocalDateTime;
import java.util.UUID;

import com.turkcell.library_cqrs.api.dto.reservation.ReservationResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

public record CreateReservationCommand(UUID studentId, UUID bookId, LocalDateTime expiresAt)
    implements Command<ReservationResponse> {
}
