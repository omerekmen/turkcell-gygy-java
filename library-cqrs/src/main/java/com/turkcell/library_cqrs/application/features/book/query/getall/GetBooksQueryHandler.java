package com.turkcell.library_cqrs.application.features.book.query.getall;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.book.BookResponse;
import com.turkcell.library_cqrs.application.features.book.mapper.BookMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.QueryHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.BookRepository;

@Component
public class GetBooksQueryHandler implements QueryHandler<GetBooksQuery, Page<BookResponse>> {

    private final BookRepository repository;

    public GetBooksQueryHandler(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<BookResponse> handle(GetBooksQuery query) {
        var pageable = PageRequest.of(query.page(), query.size());
        Page<?> result;

        if (query.title() == null || query.title().isBlank()) {
            result = repository.findByDeletedAtIsNull(pageable);
        } else {
            result = repository.findByTitleContainingIgnoreCaseAndDeletedAtIsNull(query.title(), pageable);
        }

        @SuppressWarnings("unchecked")
        Page<com.turkcell.library_cqrs.domain.entity.Book> books = (Page<com.turkcell.library_cqrs.domain.entity.Book>) result;
        return books.map(BookMapper::toDto);
    }
}
