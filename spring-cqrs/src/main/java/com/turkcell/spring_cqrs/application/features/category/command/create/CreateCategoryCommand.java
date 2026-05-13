package com.turkcell.spring_cqrs.application.features.category.command.create;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.turkcell.spring_cqrs.core.mediator.cqrs.Command;
import com.turkcell.spring_cqrs.core.security.authorization.AuthorizableRequest;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryCommand(
    @NotBlank @Length(min=3,max=100) String name
    
) implements Command<CreatedCategoryResponse>, AuthorizableRequest {
    @Override
    public List<String> requiredRoles() {
        return List.of("ADMIN");
    }
}
