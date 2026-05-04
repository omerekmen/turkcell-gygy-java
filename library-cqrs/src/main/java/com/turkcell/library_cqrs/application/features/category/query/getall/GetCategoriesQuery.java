package com.turkcell.library_cqrs.application.features.category.query.getall;

import java.util.List;

import com.turkcell.library_cqrs.api.dto.category.CategoryResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Query;

public record GetCategoriesQuery() implements Query<List<CategoryResponse>> {
}
