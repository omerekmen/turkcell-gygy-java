package com.turkcell.library_cqrs.infrastructure.persistance.postgresql;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.library_cqrs.domain.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    Page<Book> findByDeletedAtIsNull(Pageable pageable);
    Page<Book> findByTitleContainingIgnoreCaseAndDeletedAtIsNull(String title, Pageable pageable);
    Optional<Book> findByIsbnAndDeletedAtIsNull(String isbn);
}
