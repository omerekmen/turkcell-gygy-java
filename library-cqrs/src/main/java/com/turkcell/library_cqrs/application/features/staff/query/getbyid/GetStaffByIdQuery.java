package com.turkcell.library_cqrs.application.features.staff.query.getbyid;

import java.util.UUID;

import com.turkcell.library_cqrs.api.dto.staff.StaffResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Query;

public record GetStaffByIdQuery(UUID id) implements Query<StaffResponse> {
}
