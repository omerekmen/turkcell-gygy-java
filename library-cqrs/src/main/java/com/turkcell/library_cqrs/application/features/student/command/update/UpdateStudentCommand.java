package com.turkcell.library_cqrs.application.features.student.command.update;

import java.util.UUID;

import com.turkcell.library_cqrs.api.dto.student.StudentResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

public record UpdateStudentCommand(UUID id, String studentNumber, Boolean isActive) implements Command<StudentResponse> {
}
