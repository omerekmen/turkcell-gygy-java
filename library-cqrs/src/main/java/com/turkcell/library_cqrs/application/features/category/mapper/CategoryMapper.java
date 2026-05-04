package com.turkcell.library_cqrs.application.features.category.mapper;

import java.util.List;

import com.turkcell.library_cqrs.api.dto.category.CategoryResponse;
import com.turkcell.library_cqrs.domain.entity.Category;

public final class CategoryMapper {

    private CategoryMapper() {}

    public static CategoryResponse toDto(Category entity) {
        if (entity == null) return null;

        var dto = new CategoryResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    public static List<CategoryResponse> toDtoList(List<Category> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(CategoryMapper::toDto).toList();
    }
}
