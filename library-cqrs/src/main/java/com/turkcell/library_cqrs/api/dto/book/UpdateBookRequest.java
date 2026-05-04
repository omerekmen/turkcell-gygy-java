package com.turkcell.library_cqrs.api.dto.book;

import java.util.List;
import java.util.UUID;

public class UpdateBookRequest {
    private String title;
    private String isbn;
    private UUID publisherId;
    private Integer publishedYear;
    private UUID categoryId;
    private List<UUID> authorIds;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public UUID getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(UUID publisherId) {
        this.publisherId = publisherId;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public List<UUID> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(List<UUID> authorIds) {
        this.authorIds = authorIds;
    }
}
