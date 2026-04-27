package com.turkcell.library_app.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.turkcell.library_app.dto.category.CategoryResponse;
import com.turkcell.library_app.dto.category.CreateCategoryRequest;
import com.turkcell.library_app.dto.category.UpdateCategoryRequest;
import com.turkcell.library_app.entity.Category;
import com.turkcell.library_app.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponse create(CreateCategoryRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name is required");
        }
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category already exists");
        }

        Category category = new Category();
        category.setName(request.getName().trim());
        return map(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse getById(UUID id) {
        return map(findCategory(id));
    }

    @Override
    public Page<CategoryResponse> getAll(int page, int size) {
        return categoryRepository.findAll(PageRequest.of(page, size)).map(this::map);
    }

    @Override
    public CategoryResponse update(UUID id, UpdateCategoryRequest request) {
        Category category = findCategory(id);
        if (request.getName() != null && !request.getName().isBlank()) {
            String normalizedName = request.getName().trim();
            if (!category.getName().equalsIgnoreCase(normalizedName)
                && categoryRepository.existsByNameIgnoreCase(normalizedName)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Category already exists");
            }
            category.setName(normalizedName);
        }
        return map(categoryRepository.save(category));
    }

    @Override
    public void delete(UUID id) {
        Category category = findCategory(id);
        categoryRepository.delete(category);
    }

    private Category findCategory(UUID id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    private CategoryResponse map(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        return response;
    }
}
