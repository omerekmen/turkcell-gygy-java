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

import com.turkcell.library_app.dto.category.CategoryResponse;
import com.turkcell.library_app.dto.category.CreateCategoryRequest;
import com.turkcell.library_app.dto.category.UpdateCategoryRequest;
import com.turkcell.library_app.service.CategoryService;

@RestController
@RequestMapping("/api/v{version:1}/categories")
public class CategoriesController {
    private final CategoryService categoryService;

    public CategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @PostMapping
    public ApiResult<CategoryResponse> create(@RequestBody CreateCategoryRequest request) {
        CategoryResponse response = categoryService.create(request);
        return ApiResult.success(HttpStatus.CREATED.value(), "Category created successfully", response);
    }

    @GetMapping("/{id}")
    public ApiResult<CategoryResponse> getById(@PathVariable UUID id) {
        CategoryResponse response = categoryService.getById(id);
        return ApiResult.success("Category retrieved successfully", response);
    }

    @GetMapping
    public ApiResult<Page<CategoryResponse>> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        Page<CategoryResponse> response = categoryService.getAll(page, size);
        return ApiResult.success("Categories retrieved successfully", response);
    }

    @PutMapping("/{id}")
    public ApiResult<CategoryResponse> update(@PathVariable UUID id, @RequestBody UpdateCategoryRequest request) {
        CategoryResponse response = categoryService.update(id, request);
        return ApiResult.success("Category updated successfully", response);
    }
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable UUID id) {
        categoryService.delete(id);
        return ApiResult.success(HttpStatus.NO_CONTENT.value(), "Category deleted successfully");
    }
}
