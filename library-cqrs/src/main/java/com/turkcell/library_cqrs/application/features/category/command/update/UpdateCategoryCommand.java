package com.turkcell.library_cqrs.application.features.category.command.update;

import java.util.UUID;

import com.turkcell.library_cqrs.api.dto.category.CategoryResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

public record UpdateCategoryCommand(UUID id, String name) implements Command<CategoryResponse> {
}
