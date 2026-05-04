package com.turkcell.library_cqrs.application.features.student.query.getall;

import org.springframework.data.domain.Page;

import com.turkcell.library_cqrs.api.dto.student.StudentResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Query;

public record GetStudentsQuery(int page, int size) implements Query<Page<StudentResponse>> {
}
