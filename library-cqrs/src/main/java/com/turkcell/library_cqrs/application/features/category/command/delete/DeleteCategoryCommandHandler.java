package com.turkcell.library_cqrs.application.features.category.command.delete;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.CategoryRepository;

@Component
public class DeleteCategoryCommandHandler implements CommandHandler<DeleteCategoryCommand, Void> {

    private final CategoryRepository repository;

    public DeleteCategoryCommandHandler(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Void handle(DeleteCategoryCommand command) {
        if (!repository.existsById(command.id())) {
            throw new IllegalArgumentException("Category not found");
        }

        repository.deleteById(command.id());
        return null;
    }
}
