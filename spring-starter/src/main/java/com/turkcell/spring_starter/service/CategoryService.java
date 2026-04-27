package com.turkcell.spring_starter.service;

import java.util.List;
import java.util.UUID;

import com.turkcell.spring_starter.dto.CreateCategoryRequest;
import com.turkcell.spring_starter.dto.CreatedCategoryResponse;
import com.turkcell.spring_starter.dto.ListCategoryResponse;
import com.turkcell.spring_starter.dto.UpdateCategoryRequest;

public interface CategoryService {
    CreatedCategoryResponse create(CreateCategoryRequest createCategoryRequest);
    List<ListCategoryResponse> getAll();
    CreatedCategoryResponse getById(UUID id);
    CreatedCategoryResponse update(UUID id, UpdateCategoryRequest request);
    void delete(UUID id);
}
