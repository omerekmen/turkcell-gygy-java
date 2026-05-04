package com.turkcell.spring_starter.dto;

import java.util.Set;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.NotBlank;

public record CreateProductRequest(
    @NotBlank(message = "Product name is required")
    @Length(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
    String name,
    String description,
    UUID categoryId,
    Set<UUID> tagIds
) {}
