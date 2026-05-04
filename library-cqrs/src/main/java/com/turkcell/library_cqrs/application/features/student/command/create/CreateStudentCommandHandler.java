package com.turkcell.library_cqrs.application.features.student.command.create;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.student.StudentResponse;
import com.turkcell.library_cqrs.application.features.student.mapper.StudentMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.library_cqrs.domain.entity.Student;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.StudentRepository;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.UserRepository;

@Component
public class CreateStudentCommandHandler implements CommandHandler<CreateStudentCommand, StudentResponse> {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public CreateStudentCommandHandler(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public StudentResponse handle(CreateStudentCommand command) {
        if (studentRepository.existsByStudentNumber(command.studentNumber())) {
            throw new IllegalArgumentException("Student number already exists");
        }

        if (command.userId() != null && studentRepository.existsByUserId(command.userId())) {
            throw new IllegalArgumentException("Student already exists for user");
        }

        var entity = new Student();
        entity.setStudentNumber(command.studentNumber());
        entity.setIsActive(command.isActive() != null ? command.isActive() : Boolean.TRUE);

        if (command.userId() != null) {
            entity.setUser(userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found")));
        }

        var saved = studentRepository.save(entity);
        return StudentMapper.toDto(saved);
    }
}
