package com.turkcell.spring_starter.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.spring_starter.dto.ApiResult;
import com.turkcell.spring_starter.dto.CreateProductRequest;
import com.turkcell.spring_starter.dto.CreatedProductResponse;
import com.turkcell.spring_starter.dto.ListProductResponse;
import com.turkcell.spring_starter.dto.UpdateProductRequest;
import com.turkcell.spring_starter.service.ProductServiceImpl;

@RestController
@RequestMapping("/api/v{version:1}/products")
public class ProductsController {
    private final ProductServiceImpl productServiceImpl;

    public ProductsController(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }

    @PostMapping
    public ApiResult<CreatedProductResponse> create(@RequestBody CreateProductRequest request) {
        CreatedProductResponse response = productServiceImpl.create(request);
        return ApiResult.success(HttpStatus.CREATED.value(), "Product created successfully", response);
    }

    @GetMapping
    public ApiResult<List<ListProductResponse>> getAll() {
        return ApiResult.success("Products retrieved successfully", productServiceImpl.getAll());
    }

    @GetMapping("/{id}")
    public ApiResult<CreatedProductResponse> getById(@PathVariable UUID id) {
        return ApiResult.success("Product retrieved successfully", productServiceImpl.getById(id));
    }

    @PutMapping("/{id}")
    public ApiResult<CreatedProductResponse> update(@PathVariable UUID id, @RequestBody UpdateProductRequest request) {
        return ApiResult.success("Product updated successfully", productServiceImpl.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable UUID id) {
        productServiceImpl.delete(id);
        return ApiResult.success(HttpStatus.NO_CONTENT.value(), "Product deleted successfully");
    }
}
