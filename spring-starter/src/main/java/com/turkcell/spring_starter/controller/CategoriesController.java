package com.turkcell.spring_starter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public CreatedCategoryResponse create(@RequestBody CreateCategoryRequest createCategoryRequest)
    {
       return categoryServiceImpl.create(createCategoryRequest);
    }

    @GetMapping
    public List<ListCategoryResponse> getAll() {
        return categoryServiceImpl.getAll();
    }

    @GetMapping("/{id}")
    public CreatedCategoryResponse getById(UUID id) {
        return categoryServiceImpl.getById(id);
    }

    @PostMapping("/{id}")
    public CreatedCategoryResponse update(UUID id, @RequestBody CreateCategoryRequest request) {
        return categoryServiceImpl.update(id, request);
    }

    @GetMapping("/search")
    public List<ListCategoryResponse> search(@RequestParam String name) {
        return categoryServiceImpl.search(name);
    }

    @DeleteMapping("/{id}")
    public void delete(UUID id) {
        categoryServiceImpl.delete(id);
    }
}