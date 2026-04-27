package com.turkcell.library_app.repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.library_app.entity.BookCopy;
import com.turkcell.library_app.enums.BookCopyStatus;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, UUID> {
    List<BookCopy> findByIdIn(Collection<UUID> ids);
    long countByBookIdAndStatus(UUID bookId, BookCopyStatus status);
}
