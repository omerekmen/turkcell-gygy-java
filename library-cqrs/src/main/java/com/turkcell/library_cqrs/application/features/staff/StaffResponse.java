package com.turkcell.library_cqrs.application.features.staff;

import java.util.UUID;

import com.turkcell.library_cqrs.domain.enums.StaffRole;

public record StaffResponse(
    UUID id,
    UUID userId,
    String staffNumber,
    StaffRole role,
    Boolean isActive
) {}