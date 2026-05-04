package com.turkcell.library_cqrs.application.features.student.command.create;

import java.util.UUID;

import com.turkcell.library_cqrs.api.dto.student.StudentResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

public record CreateStudentCommand(UUID userId, String studentNumber, Boolean isActive) implements Command<StudentResponse> {
}
