package com.turkcell.library_cqrs.application.features.category.query.getbyid;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.category.CategoryResponse;
import com.turkcell.library_cqrs.application.features.category.mapper.CategoryMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.QueryHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.CategoryRepository;

@Component
public class GetCategoryByIdQueryHandler implements QueryHandler<GetCategoryByIdQuery, CategoryResponse> {

    private final CategoryRepository repository;

    public GetCategoryByIdQueryHandler(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public CategoryResponse handle(GetCategoryByIdQuery query) {
        var entity = repository.findById(query.id())
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        return CategoryMapper.toDto(entity);
    }
}
