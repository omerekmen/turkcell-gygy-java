package com.turkcell.library_cqrs.application.features.book.command.update;

import java.util.List;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import com.turkcell.library_cqrs.application.features.book.BookResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateBookCommand(
    @NotNull UUID id,
    @NotBlank @Length(min = 1, max = 255) String title,
    @NotBlank @Length(min = 1, max = 64) String isbn,
    UUID publisherId,
    Integer publishedYear,
    UUID categoryId,
    List<UUID> authorIds
) implements Command<BookResponse> {
}
