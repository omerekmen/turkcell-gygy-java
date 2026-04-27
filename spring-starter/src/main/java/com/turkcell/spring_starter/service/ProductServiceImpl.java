package com.turkcell.spring_starter.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.turkcell.spring_starter.dto.CreateProductRequest;
import com.turkcell.spring_starter.dto.CreatedProductResponse;
import com.turkcell.spring_starter.dto.ListProductResponse;
import com.turkcell.spring_starter.dto.UpdateProductRequest;
import com.turkcell.spring_starter.entity.Category;
import com.turkcell.spring_starter.entity.Product;
import com.turkcell.spring_starter.entity.Tag;
import com.turkcell.spring_starter.repository.CategoryRepository;
import com.turkcell.spring_starter.repository.ProductRepository;
import com.turkcell.spring_starter.repository.TagRepository;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, TagRepository tagRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    public CreatedProductResponse create(CreateProductRequest request) {
        Product product = new Product();
        applyProductValues(product, request.getName(), request.getDescription(), request.getCategoryId(), request.getTagIds());
        return mapCreated(productRepository.save(product));
    }

    public List<ListProductResponse> getAll() {
        return productRepository.findAll()
            .stream()
            .map(this::mapList)
            .toList();
    }

    public CreatedProductResponse getById(UUID id) {
        Product product = findProduct(id);
        return mapCreated(product);
    }

    public CreatedProductResponse update(UUID id, UpdateProductRequest request) {
        Product product = findProduct(id);

        applyProductValues(
            product,
            request.getName() != null ? request.getName() : product.getName(),
            request.getDescription() != null ? request.getDescription() : product.getDescription(),
            request.getCategoryId() != null ? request.getCategoryId() : product.getCategory().getId(),
            request.getTagIds() != null ? request.getTagIds() : extractTagIds(product.getTags())
        );

        return mapCreated(productRepository.save(product));
    }

    public void delete(UUID id) {
        Product product = findProduct(id);
        productRepository.delete(product);
    }

    private Product findProduct(UUID id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    private void applyProductValues(Product product, String name, String description, UUID categoryId, Set<UUID> tagIds) {
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name is required");
        }
        if (categoryId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "categoryId is required");
        }

        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found"));

        Set<Tag> tags = new HashSet<>();
        if (tagIds != null && !tagIds.isEmpty()) {
            tags = new HashSet<>(tagRepository.findAllById(tagIds));
            if (tags.size() != tagIds.size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more tags not found");
            }
        }

        product.setName(name.trim());
        product.setDescription(description);
        product.setCategory(category);
        product.setTags(tags);
    }

    private Set<UUID> extractTagIds(Set<Tag> tags) {
        if (tags == null) {
            return Set.of();
        }
        return tags.stream().map(Tag::getId).collect(java.util.stream.Collectors.toSet());
    }

    private CreatedProductResponse mapCreated(Product product) {
        CreatedProductResponse response = new CreatedProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setCategoryId(product.getCategory() == null ? null : product.getCategory().getId());
        response.setTagIds(extractTagIds(product.getTags()));
        return response;
    }

    private ListProductResponse mapList(Product product) {
        ListProductResponse response = new ListProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setCategoryId(product.getCategory() == null ? null : product.getCategory().getId());
        response.setTagIds(extractTagIds(product.getTags()));
        return response;
    }
}
