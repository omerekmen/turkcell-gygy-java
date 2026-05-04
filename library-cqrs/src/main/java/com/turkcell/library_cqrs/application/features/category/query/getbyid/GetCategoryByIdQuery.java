package com.turkcell.library_cqrs.application.features.category.query.getbyid;

import java.util.UUID;

import com.turkcell.library_cqrs.api.dto.category.CategoryResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Query;

public record GetCategoryByIdQuery(UUID id) implements Query<CategoryResponse> {
}
