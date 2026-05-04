package com.turkcell.library_cqrs.application.features.book.command.create;

import java.util.List;
import java.util.UUID;

import com.turkcell.library_cqrs.api.dto.book.BookResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

public record CreateBookCommand(
    String title,
    String isbn,
    UUID publisherId,
    Integer publishedYear,
    UUID categoryId,
    List<UUID> authorIds
) implements Command<BookResponse> {
}
