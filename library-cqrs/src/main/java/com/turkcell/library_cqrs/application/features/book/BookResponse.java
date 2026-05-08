package com.turkcell.library_cqrs.application.features.book;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record BookResponse(
    UUID id,
    String title,
    String isbn,
    UUID publisherId,
    Integer publishedYear,
    UUID categoryId,
    List<UUID> authorIds,
    LocalDateTime deletedAt
) {}