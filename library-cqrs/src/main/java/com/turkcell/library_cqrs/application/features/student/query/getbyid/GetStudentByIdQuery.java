package com.turkcell.library_cqrs.application.features.student.query.getbyid;

import java.util.UUID;

import com.turkcell.library_cqrs.api.dto.student.StudentResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Query;

public record GetStudentByIdQuery(UUID id) implements Query<StudentResponse> {
}
