package com.turkcell.library_cqrs.application.features.book.command.delete;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.BookRepository;

@Component
public class DeleteBookCommandHandler implements CommandHandler<DeleteBookCommand, Void> {

    private final BookRepository repository;

    public DeleteBookCommandHandler(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Void handle(DeleteBookCommand command) {
        var entity = repository.findById(command.id())
            .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        entity.setDeletedAt(LocalDateTime.now());
        repository.save(entity);
        return null;
    }
}
