package com.turkcell.library_cqrs.application.features.category.command.create;

import org.hibernate.validator.constraints.Length;

import com.turkcell.library_cqrs.application.features.category.CategoryResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryCommand(@NotBlank @Length(min = 3, max = 100) String name) implements Command<CategoryResponse> {

}
