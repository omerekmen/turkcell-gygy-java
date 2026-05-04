package com.turkcell.library_cqrs.application.features.book.mapper;

import java.util.List;

import com.turkcell.library_cqrs.api.dto.book.BookResponse;
import com.turkcell.library_cqrs.domain.entity.Book;

public final class BookMapper {

    private BookMapper() {
    }

    public static BookResponse toDto(Book entity) {
        if (entity == null) {
            return null;
        }

        var dto = new BookResponse();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setIsbn(entity.getIsbn());
        dto.setPublisherId(entity.getPublisher() != null ? entity.getPublisher().getId() : null);
        dto.setPublishedYear(entity.getPublishedYear());
        dto.setCategoryId(entity.getCategory() != null ? entity.getCategory().getId() : null);
        dto.setDeletedAt(entity.getDeletedAt());

        List<java.util.UUID> authorIds = entity.getAuthors() == null
            ? List.of()
            : entity.getAuthors().stream().map(a -> a.getId()).toList();
        dto.setAuthorIds(authorIds);

        return dto;
    }

    public static List<BookResponse> toDtoList(List<Book> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream().map(BookMapper::toDto).toList();
    }
}
