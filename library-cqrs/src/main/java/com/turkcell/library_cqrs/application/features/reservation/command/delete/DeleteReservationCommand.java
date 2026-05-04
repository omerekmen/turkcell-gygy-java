package com.turkcell.library_cqrs.application.features.reservation.command.delete;

import java.util.UUID;

import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

public record DeleteReservationCommand(UUID id) implements Command<Void> {
}
