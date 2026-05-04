package com.turkcell.library_cqrs.application.features.staff.command.delete;

import java.util.UUID;

import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

public record DeleteStaffCommand(UUID id) implements Command<Void> {
}
