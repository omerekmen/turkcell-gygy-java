package com.turkcell.library_cqrs.application.features.book.command.delete;

import java.util.UUID;

import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

public record DeleteBookCommand(UUID id) implements Command<Void> {
}
