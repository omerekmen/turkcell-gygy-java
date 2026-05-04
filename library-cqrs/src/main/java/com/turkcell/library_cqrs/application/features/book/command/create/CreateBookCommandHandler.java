package com.turkcell.library_cqrs.application.features.book.command.create;

import java.util.HashSet;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.book.BookResponse;
import com.turkcell.library_cqrs.application.features.book.mapper.BookMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.library_cqrs.domain.entity.Book;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.AuthorRepository;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.BookRepository;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.CategoryRepository;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.PublisherRepository;

@Component
public class CreateBookCommandHandler implements CommandHandler<CreateBookCommand, BookResponse> {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;

    public CreateBookCommandHandler(
        BookRepository bookRepository,
        PublisherRepository publisherRepository,
        CategoryRepository categoryRepository,
        AuthorRepository authorRepository
    ) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.categoryRepository = categoryRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public BookResponse handle(CreateBookCommand command) {
        var entity = new Book();
        entity.setTitle(command.title());
        entity.setIsbn(command.isbn());
        entity.setPublishedYear(command.publishedYear());

        if (command.publisherId() != null) {
            entity.setPublisher(
                publisherRepository.findById(command.publisherId())
                    .orElseThrow(() -> new IllegalArgumentException("Publisher not found"))
            );
        }

        if (command.categoryId() != null) {
            entity.setCategory(
                categoryRepository.findById(command.categoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"))
            );
        }

        if (command.authorIds() != null && !command.authorIds().isEmpty()) {
            var authors = new HashSet<>(authorRepository.findAllById(command.authorIds()));
            entity.setAuthors(authors);
        }

        var saved = bookRepository.save(entity);
        return BookMapper.toDto(saved);
    }
}
