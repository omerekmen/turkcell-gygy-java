package com.turkcell.spring_starter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.turkcell.spring_starter.dto.ApiResult;
import com.turkcell.spring_starter.dto.CreateCategoryRequest;
import com.turkcell.spring_starter.dto.CreatedCategoryResponse;
import com.turkcell.spring_starter.dto.ListCategoryResponse;
import com.turkcell.spring_starter.service.CategoryServiceImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v{version:1}/categories")
public class CategoriesController {
    private final CategoryServiceImpl categoryServiceImpl;

    public CategoriesController(CategoryServiceImpl categoryServiceImpl) {
        this.categoryServiceImpl = categoryServiceImpl;
    }

    @PostMapping()
    public ApiResult<CreatedCategoryResponse> create(@RequestBody CreateCategoryRequest createCategoryRequest)
    {
       return ApiResult.success(HttpStatus.CREATED.value(), "Category created successfully", categoryServiceImpl.create(createCategoryRequest));
    }

    @GetMapping
    public ApiResult<List<ListCategoryResponse>> getAll() {
        return ApiResult.success("Categories retrieved successfully", categoryServiceImpl.getAll());
    }

    @GetMapping("/{id}")
    public ApiResult<CreatedCategoryResponse> getById(@PathVariable UUID id) {
        return ApiResult.success("Category retrieved successfully", categoryServiceImpl.getById(id));
    }

    @PostMapping("/{id}")
    public ApiResult<CreatedCategoryResponse> update(@PathVariable UUID id, @RequestBody CreateCategoryRequest request) {
        return ApiResult.success("Category updated successfully", categoryServiceImpl.update(id, request));
    }

    @GetMapping("/search")
    public ApiResult<List<ListCategoryResponse>> search(@RequestParam String name) {
        return ApiResult.success("Categories searched successfully", categoryServiceImpl.search(name));
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable UUID id) {
        categoryServiceImpl.delete(id);
        return ApiResult.success(HttpStatus.NO_CONTENT.value(), "Category deleted successfully");
    }
}