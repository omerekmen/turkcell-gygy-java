package com.turkcell.library_cqrs.application.features.book.command.update;

import java.util.List;
import java.util.UUID;

import com.turkcell.library_cqrs.api.dto.book.BookResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

public record UpdateBookCommand(
    UUID id,
    String title,
    String isbn,
    UUID publisherId,
    Integer publishedYear,
    UUID categoryId,
    List<UUID> authorIds
) implements Command<BookResponse> {
}
