package com.turkcell.library_cqrs.application.features.category.mapper;

import java.util.List;

import com.turkcell.library_cqrs.application.features.category.CategoryResponse;
import com.turkcell.library_cqrs.domain.entity.Category;

public final class CategoryMapper {

    private CategoryMapper() {}

    public static CategoryResponse toDto(Category entity) {
        if (entity == null) return null;

        return new CategoryResponse(entity.getId(), entity.getName());
    }

    public static List<CategoryResponse> toDtoList(List<Category> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(CategoryMapper::toDto).toList();
    }
}
