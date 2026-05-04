package com.turkcell.library_cqrs.application.features.category.command.create;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.application.features.category.mapper.CategoryMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.library_cqrs.api.dto.category.CategoryResponse;
import com.turkcell.library_cqrs.domain.entity.Category;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.CategoryRepository;

@Component
public class CreateCategoryCommandHandler implements CommandHandler<CreateCategoryCommand, CategoryResponse> {

    private final CategoryRepository repository;

    public CreateCategoryCommandHandler(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public CategoryResponse handle(CreateCategoryCommand command) {
        var entity = new Category();
        entity.setName(command.name());

        var saved = repository.save(entity);

        return CategoryMapper.toDto(saved);
    }

}
