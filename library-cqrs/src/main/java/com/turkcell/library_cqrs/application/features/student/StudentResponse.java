package com.turkcell.library_cqrs.application.features.student;

import java.util.UUID;

public record StudentResponse(
    UUID id,
    UUID userId,
    String studentNumber,
    Boolean isActive
) {}