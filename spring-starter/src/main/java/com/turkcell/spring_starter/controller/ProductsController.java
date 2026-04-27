package com.turkcell.spring_starter.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public CreatedProductResponse create(@RequestBody CreateProductRequest request) {
        return productServiceImpl.create(request);
    }

    @GetMapping
    public List<ListProductResponse> getAll() {
        return productServiceImpl.getAll();
    }

    @GetMapping("/{id}")
    public CreatedProductResponse getById(@PathVariable UUID id) {
        return productServiceImpl.getById(id);
    }

    @PutMapping("/{id}")
    public CreatedProductResponse update(@PathVariable UUID id, @RequestBody UpdateProductRequest request) {
        return productServiceImpl.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        productServiceImpl.delete(id);
    }
}
