package com.turkcell.library_app.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.turkcell.library_app.dto.book.BookResponse;
import com.turkcell.library_app.dto.book.CreateBookRequest;
import com.turkcell.library_app.dto.book.UpdateBookRequest;

public interface BookService {
    BookResponse create(CreateBookRequest request);
    BookResponse getById(UUID id);
    Page<BookResponse> getAll(String title, int page, int size);
    BookResponse update(UUID id, UpdateBookRequest request);
    void delete(UUID id);
}
