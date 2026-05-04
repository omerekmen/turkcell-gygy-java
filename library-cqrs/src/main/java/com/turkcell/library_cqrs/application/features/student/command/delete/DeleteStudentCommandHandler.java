package com.turkcell.library_cqrs.application.features.student.command.delete;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.StudentRepository;

@Component
public class DeleteStudentCommandHandler implements CommandHandler<DeleteStudentCommand, Void> {

    private final StudentRepository repository;

    public DeleteStudentCommandHandler(StudentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Void handle(DeleteStudentCommand command) {
        if (!repository.existsById(command.id())) {
            throw new IllegalArgumentException("Student not found");
        }

        repository.deleteById(command.id());
        return null;
    }
}
