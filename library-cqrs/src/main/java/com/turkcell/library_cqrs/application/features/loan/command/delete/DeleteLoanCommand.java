package com.turkcell.library_cqrs.application.features.loan.command.delete;

import java.util.UUID;

import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

public record DeleteLoanCommand(UUID id) implements Command<Void> {
}
