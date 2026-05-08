package com.turkcell.library_cqrs.application.features.book.mapper;

import java.util.List;

import com.turkcell.library_cqrs.application.features.book.BookResponse;
import com.turkcell.library_cqrs.domain.entity.Book;

public final class BookMapper {

    private BookMapper() {
    }

    public static BookResponse toDto(Book entity) {
        if (entity == null) {
            return null;
        }

        List<java.util.UUID> authorIds = entity.getAuthors() == null
            ? List.of()
            : entity.getAuthors().stream().map(a -> a.getId()).toList();

        return new BookResponse(
            entity.getId(),
            entity.getTitle(),
            entity.getIsbn(),
            entity.getPublisher() != null ? entity.getPublisher().getId() : null,
            entity.getPublishedYear(),
            entity.getCategory() != null ? entity.getCategory().getId() : null,
            authorIds,
            entity.getDeletedAt()
        );
    }

    public static List<BookResponse> toDtoList(List<Book> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream().map(BookMapper::toDto).toList();
    }
}
