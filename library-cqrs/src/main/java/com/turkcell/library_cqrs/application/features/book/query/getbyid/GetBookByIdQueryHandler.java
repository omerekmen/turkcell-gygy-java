package com.turkcell.library_cqrs.application.features.book.query.getbyid;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.book.BookResponse;
import com.turkcell.library_cqrs.application.features.book.mapper.BookMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.QueryHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.BookRepository;

@Component
public class GetBookByIdQueryHandler implements QueryHandler<GetBookByIdQuery, BookResponse> {

    private final BookRepository repository;

    public GetBookByIdQueryHandler(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public BookResponse handle(GetBookByIdQuery query) {
        var entity = repository.findById(query.id())
            .filter(book -> book.getDeletedAt() == null)
            .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        return BookMapper.toDto(entity);
    }
}
