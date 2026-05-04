package com.turkcell.library_cqrs.application.features.category.query.getall;

import java.util.List;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.category.CategoryResponse;
import com.turkcell.library_cqrs.application.features.category.mapper.CategoryMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.QueryHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.CategoryRepository;

@Component
public class GetCategoriesQueryHandler implements QueryHandler<GetCategoriesQuery, List<CategoryResponse>> {

    private final CategoryRepository repository;

    public GetCategoriesQueryHandler(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CategoryResponse> handle(GetCategoriesQuery query) {
        return CategoryMapper.toDtoList(repository.findAll());
    }
}
