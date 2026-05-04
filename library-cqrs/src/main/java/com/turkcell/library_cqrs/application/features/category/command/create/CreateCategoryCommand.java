package com.turkcell.library_cqrs.application.features.category.command.create;

import com.turkcell.library_cqrs.api.dto.category.CategoryResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

public record CreateCategoryCommand(String name) implements Command<CategoryResponse> {

}
