package com.turkcell.library_cqrs.application.features.book.query.getbyid;

import java.util.UUID;

import com.turkcell.library_cqrs.api.dto.book.BookResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Query;

public record GetBookByIdQuery(UUID id) implements Query<BookResponse> {
}
