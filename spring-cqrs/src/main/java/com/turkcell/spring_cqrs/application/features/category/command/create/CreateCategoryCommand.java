package com.turkcell.spring_cqrs.application.features.category.command.create;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.turkcell.spring_cqrs.core.mediator.cqrs.Command;

@Component
public record CreateCategoryCommand(String name, String description) implements Command<UUID> {

}
