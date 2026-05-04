package com.turkcell.library_cqrs.application.features.staff.query.getall;

import org.springframework.data.domain.Page;

import com.turkcell.library_cqrs.api.dto.staff.StaffResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Query;

public record GetStaffsQuery(int page, int size) implements Query<Page<StaffResponse>> {
}
