package com.turkcell.library_cqrs.application.features.category.command.update;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.category.CategoryResponse;
import com.turkcell.library_cqrs.application.features.category.mapper.CategoryMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.CategoryRepository;

@Component
public class UpdateCategoryCommandHandler implements CommandHandler<UpdateCategoryCommand, CategoryResponse> {

    private final CategoryRepository repository;

    public UpdateCategoryCommandHandler(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public CategoryResponse handle(UpdateCategoryCommand command) {
        var entity = repository.findById(command.id())
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        entity.setName(command.name());

        return CategoryMapper.toDto(repository.save(entity));
    }
}
