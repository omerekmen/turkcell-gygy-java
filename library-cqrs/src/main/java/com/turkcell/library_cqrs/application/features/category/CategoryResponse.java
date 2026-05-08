package com.turkcell.library_cqrs.application.features.category;

import java.util.UUID;

public record CategoryResponse(UUID id, String name) {}