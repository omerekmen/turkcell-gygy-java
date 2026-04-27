package com.turkcell.library_app.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.turkcell.library_app.dto.book.BookResponse;
import com.turkcell.library_app.dto.book.CreateBookRequest;
import com.turkcell.library_app.dto.book.UpdateBookRequest;
import com.turkcell.library_app.entity.Author;
import com.turkcell.library_app.entity.Book;
import com.turkcell.library_app.entity.Category;
import com.turkcell.library_app.entity.Publisher;
import com.turkcell.library_app.repository.AuthorRepository;
import com.turkcell.library_app.repository.BookRepository;
import com.turkcell.library_app.repository.CategoryRepository;
import com.turkcell.library_app.repository.PublisherRepository;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;

    public BookServiceImpl(
        BookRepository bookRepository,
        PublisherRepository publisherRepository,
        CategoryRepository categoryRepository,
        AuthorRepository authorRepository
    ) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.categoryRepository = categoryRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public BookResponse create(CreateBookRequest request) {
        validateBookRequest(request.getTitle(), request.getIsbn(), request.getPublishedYear());
        if (request.getIsbn() != null && bookRepository.findByIsbnAndDeletedAtIsNull(request.getIsbn()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ISBN already exists");
        }

        Book book = new Book();
        applyBookFields(book, request.getTitle(), request.getIsbn(), request.getPublisherId(), request.getPublishedYear(), request.getCategoryId(), request.getAuthorIds());
        return map(bookRepository.save(book));
    }

    @Override
    public BookResponse getById(UUID id) {
        return map(findActiveBook(id));
    }

    @Override
    public Page<BookResponse> getAll(String title, int page, int size) {
        if (title == null || title.isBlank()) {
            return bookRepository.findByDeletedAtIsNull(PageRequest.of(page, size)).map(this::map);
        }
        return bookRepository.findByTitleContainingIgnoreCaseAndDeletedAtIsNull(title, PageRequest.of(page, size)).map(this::map);
    }

    @Override
    public BookResponse update(UUID id, UpdateBookRequest request) {
        Book book = findActiveBook(id);
        String nextTitle = request.getTitle() != null ? request.getTitle() : book.getTitle();
        String nextIsbn = request.getIsbn() != null ? request.getIsbn() : book.getIsbn();
        Integer nextYear = request.getPublishedYear() != null ? request.getPublishedYear() : book.getPublishedYear();

        validateBookRequest(nextTitle, nextIsbn, nextYear);

        if (nextIsbn != null) {
            bookRepository.findByIsbnAndDeletedAtIsNull(nextIsbn)
                .filter(existing -> !existing.getId().equals(book.getId()))
                .ifPresent(existing -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "ISBN already exists");
                });
        }

        applyBookFields(book, nextTitle, nextIsbn, request.getPublisherId(), nextYear, request.getCategoryId(), request.getAuthorIds());
        return map(bookRepository.save(book));
    }

    @Override
    public void delete(UUID id) {
        Book book = findActiveBook(id);
        book.setDeletedAt(LocalDateTime.now());
        bookRepository.save(book);
    }

    private void applyBookFields(Book book, String title, String isbn, UUID publisherId, Integer publishedYear, UUID categoryId, List<UUID> authorIds) {
        book.setTitle(title.trim());
        book.setIsbn(isbn == null || isbn.isBlank() ? null : isbn.trim());
        book.setPublishedYear(publishedYear);

        if (publisherId != null) {
            Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Publisher not found"));
            book.setPublisher(publisher);
        }

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found"));
            book.setCategory(category);
        }

        if (authorIds != null) {
            Set<Author> authors = new HashSet<>(authorRepository.findAllById(authorIds));
            if (authors.size() != authorIds.size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more authors not found");
            }
            book.setAuthors(authors);
        }
    }

    private void validateBookRequest(String title, String isbn, Integer publishedYear) {
        if (title == null || title.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book title is required");
        }
        if (publishedYear != null && (publishedYear < 1000 || publishedYear > LocalDateTime.now().getYear() + 1)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Published year is out of range");
        }
        if (isbn != null && isbn.length() > 50) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ISBN max length is 50");
        }
    }

    private Book findActiveBook(UUID id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        if (book.getDeletedAt() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
        return book;
    }

    private BookResponse map(Book book) {
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setIsbn(book.getIsbn());
        response.setDeletedAt(book.getDeletedAt());
        response.setPublishedYear(book.getPublishedYear());
        response.setPublisherId(book.getPublisher() == null ? null : book.getPublisher().getId());
        response.setCategoryId(book.getCategory() == null ? null : book.getCategory().getId());
        List<UUID> authorIds = new ArrayList<>();
        if (book.getAuthors() != null) {
            for (Author author : book.getAuthors()) {
                authorIds.add(author.getId());
            }
        }
        response.setAuthorIds(authorIds);
        return response;
    }
}
