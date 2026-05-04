package com.turkcell.library_cqrs.application.features.book.query.getall;

import org.springframework.data.domain.Page;

import com.turkcell.library_cqrs.api.dto.book.BookResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Query;

public record GetBooksQuery(String title, int page, int size) implements Query<Page<BookResponse>> {
}
