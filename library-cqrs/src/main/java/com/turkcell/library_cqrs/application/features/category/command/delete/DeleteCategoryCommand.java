package com.turkcell.library_cqrs.application.features.category.command.delete;

import java.util.UUID;

import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

public record DeleteCategoryCommand(UUID id) implements Command<Void> {
}
