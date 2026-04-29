package com.turkcell.library_app.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.library_app.dto.ApiResult;

import com.turkcell.library_app.dto.book.BookResponse;
import com.turkcell.library_app.dto.book.CreateBookRequest;
import com.turkcell.library_app.dto.book.UpdateBookRequest;
import com.turkcell.library_app.service.BookService;

@RestController
@RequestMapping("/api/v{version:1}/books")
public class BooksController {
    private final BookService bookService;

    public BooksController(BookService bookService) {
        this.bookService = bookService;
    }


    @PostMapping
    public ApiResult<BookResponse> create(@RequestBody CreateBookRequest request) {
        BookResponse response = bookService.create(request);
        return ApiResult.success(HttpStatus.CREATED.value(), "Book created successfully", response);
    }

    @GetMapping("/{id}")
    public ApiResult<BookResponse> getById(@PathVariable UUID id) {
        BookResponse response = bookService.getById(id);
        return ApiResult.success("Book retrieved successfully", response);
    }

    @GetMapping
    public ApiResult<Page<BookResponse>> getAll(
        @RequestParam(required = false) String title,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        Page<BookResponse> response = bookService.getAll(title, page, size);
        return ApiResult.success("Books retrieved successfully", response);
    }

    @PutMapping("/{id}")
    public ApiResult<BookResponse> update(@PathVariable UUID id, @RequestBody UpdateBookRequest request) {
        BookResponse response = bookService.update(id, request);
        return ApiResult.success("Book updated successfully", response);
    }
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable UUID id) {
        bookService.delete(id);
        return ApiResult.success(HttpStatus.NO_CONTENT.value(), "Book deleted successfully");
    }
}
    