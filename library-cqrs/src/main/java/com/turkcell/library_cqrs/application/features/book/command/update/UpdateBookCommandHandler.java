package com.turkcell.library_cqrs.application.features.book.command.update;

import java.util.HashSet;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.book.BookResponse;
import com.turkcell.library_cqrs.application.features.book.mapper.BookMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.AuthorRepository;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.BookRepository;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.CategoryRepository;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.PublisherRepository;

@Component
public class UpdateBookCommandHandler implements CommandHandler<UpdateBookCommand, BookResponse> {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;

    public UpdateBookCommandHandler(
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
    public BookResponse handle(UpdateBookCommand command) {
        var entity = bookRepository.findById(command.id())
            .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        entity.setTitle(command.title());
        entity.setIsbn(command.isbn());
        entity.setPublishedYear(command.publishedYear());

        entity.setPublisher(command.publisherId() == null ? null :
            publisherRepository.findById(command.publisherId())
                .orElseThrow(() -> new IllegalArgumentException("Publisher not found")));

        entity.setCategory(command.categoryId() == null ? null :
            categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found")));

        if (command.authorIds() != null) {
            entity.setAuthors(new HashSet<>(authorRepository.findAllById(command.authorIds())));
        }

        return BookMapper.toDto(bookRepository.save(entity));
    }
}
