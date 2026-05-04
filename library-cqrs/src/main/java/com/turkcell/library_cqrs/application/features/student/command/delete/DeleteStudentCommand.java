package com.turkcell.library_cqrs.application.features.student.command.delete;

import java.util.UUID;

import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

public record DeleteStudentCommand(UUID id) implements Command<Void> {
}
