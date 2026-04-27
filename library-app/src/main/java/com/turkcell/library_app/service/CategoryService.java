package com.turkcell.library_app.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.turkcell.library_app.dto.category.CategoryResponse;
import com.turkcell.library_app.dto.category.CreateCategoryRequest;
import com.turkcell.library_app.dto.category.UpdateCategoryRequest;

public interface CategoryService {
    CategoryResponse create(CreateCategoryRequest request);
    CategoryResponse getById(UUID id);
    Page<CategoryResponse> getAll(int page, int size);
    CategoryResponse update(UUID id, UpdateCategoryRequest request);
    void delete(UUID id);
}
