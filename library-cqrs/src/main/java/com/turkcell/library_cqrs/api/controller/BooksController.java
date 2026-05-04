package com.turkcell.library_cqrs.api.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.library_cqrs.api.dto.ApiResult;
import com.turkcell.library_cqrs.api.dto.book.BookResponse;
import com.turkcell.library_cqrs.api.dto.book.CreateBookRequest;
import com.turkcell.library_cqrs.api.dto.book.UpdateBookRequest;
import com.turkcell.library_cqrs.application.features.book.command.create.CreateBookCommand;
import com.turkcell.library_cqrs.application.features.book.command.delete.DeleteBookCommand;
import com.turkcell.library_cqrs.application.features.book.command.update.UpdateBookCommand;
import com.turkcell.library_cqrs.application.features.book.query.getall.GetBooksQuery;
import com.turkcell.library_cqrs.application.features.book.query.getbyid.GetBookByIdQuery;
import com.turkcell.library_cqrs.core.mediator.Mediator;

@RestController
@RequestMapping("api/v{version:1}/books")
public class BooksController {

    private final Mediator mediator;

    public BooksController(Mediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping
    public ResponseEntity<ApiResult<BookResponse>> create(@RequestBody CreateBookRequest request) {
        var dto = mediator.send(new CreateBookCommand(
            request.getTitle(),
            request.getIsbn(),
            request.getPublisherId(),
            request.getPublishedYear(),
            request.getCategoryId(),
            request.getAuthorIds()
        ));

        return ResponseEntity.created(URI.create("/books/" + dto.getId()))
            .body(ApiResult.success(HttpStatus.CREATED.value(), "Book created successfully", dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<BookResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResult.success("Book retrieved successfully", mediator.send(new GetBookByIdQuery(id))));
    }

    @GetMapping
    public ResponseEntity<ApiResult<Page<BookResponse>>> getAll(
        @RequestParam(required = false) String title,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(ApiResult.success("Books retrieved successfully", mediator.send(new GetBooksQuery(title, page, size))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<BookResponse>> update(@PathVariable UUID id, @RequestBody UpdateBookRequest request) {
        return ResponseEntity.ok(ApiResult.success("Book updated successfully", mediator.send(new UpdateBookCommand(
            id,
            request.getTitle(),
            request.getIsbn(),
            request.getPublisherId(),
            request.getPublishedYear(),
            request.getCategoryId(),
            request.getAuthorIds()
        ))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> delete(@PathVariable UUID id) {
        mediator.send(new DeleteBookCommand(id));
        return ResponseEntity.ok(ApiResult.success(HttpStatus.NO_CONTENT.value(), "Book deleted successfully"));
    }
}
