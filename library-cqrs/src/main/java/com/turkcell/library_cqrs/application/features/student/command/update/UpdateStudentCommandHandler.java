package com.turkcell.library_cqrs.application.features.student.command.update;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.student.StudentResponse;
import com.turkcell.library_cqrs.application.features.student.mapper.StudentMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.StudentRepository;

@Component
public class UpdateStudentCommandHandler implements CommandHandler<UpdateStudentCommand, StudentResponse> {

    private final StudentRepository repository;

    public UpdateStudentCommandHandler(StudentRepository repository) {
        this.repository = repository;
    }

    @Override
    public StudentResponse handle(UpdateStudentCommand command) {
        var entity = repository.findById(command.id())
            .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        entity.setStudentNumber(command.studentNumber());
        if (command.isActive() != null) {
            entity.setIsActive(command.isActive());
        }

        return StudentMapper.toDto(repository.save(entity));
    }
}
