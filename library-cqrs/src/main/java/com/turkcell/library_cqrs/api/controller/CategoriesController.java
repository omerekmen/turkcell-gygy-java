package com.turkcell.library_cqrs.api.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.library_cqrs.api.dto.ApiResult;
import com.turkcell.library_cqrs.api.dto.category.UpdateCategoryRequest;
import com.turkcell.library_cqrs.application.features.category.command.create.CreateCategoryCommand;
import com.turkcell.library_cqrs.application.features.category.command.delete.DeleteCategoryCommand;
import com.turkcell.library_cqrs.application.features.category.command.update.UpdateCategoryCommand;
import com.turkcell.library_cqrs.application.features.category.query.getall.GetCategoriesQuery;
import com.turkcell.library_cqrs.application.features.category.query.getbyid.GetCategoryByIdQuery;
import com.turkcell.library_cqrs.api.dto.category.CategoryResponse;
import com.turkcell.library_cqrs.api.dto.category.CreateCategoryRequest;
import com.turkcell.library_cqrs.core.mediator.Mediator;

@RestController
@RequestMapping("api/v{version:1}/categories")
public class CategoriesController {

    private final Mediator mediator;

    public CategoriesController(Mediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping
    public ResponseEntity<ApiResult<CategoryResponse>> create(@RequestBody CreateCategoryRequest request) {
        CategoryResponse dto = mediator.send(new CreateCategoryCommand(request.getName()));
        return ResponseEntity.created(URI.create("/categories/" + dto.getId()))
            .body(ApiResult.success(HttpStatus.CREATED.value(), "Category created successfully", dto));
    }

    @GetMapping
    public ResponseEntity<ApiResult<java.util.List<CategoryResponse>>> getAll() {
        return ResponseEntity.ok(ApiResult.success("Categories retrieved successfully", mediator.send(new GetCategoriesQuery())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<CategoryResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResult.success("Category retrieved successfully", mediator.send(new GetCategoryByIdQuery(id))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<CategoryResponse>> update(@PathVariable UUID id, @RequestBody UpdateCategoryRequest request) {
        return ResponseEntity.ok(ApiResult.success("Category updated successfully", mediator.send(new UpdateCategoryCommand(id, request.getName()))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> delete(@PathVariable UUID id) {
        mediator.send(new DeleteCategoryCommand(id));
        return ResponseEntity.ok(ApiResult.success(200, "Category deleted successfully"));
    }

}
