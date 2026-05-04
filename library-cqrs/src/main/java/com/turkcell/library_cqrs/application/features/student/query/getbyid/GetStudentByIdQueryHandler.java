package com.turkcell.library_cqrs.application.features.student.query.getbyid;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.student.StudentResponse;
import com.turkcell.library_cqrs.application.features.student.mapper.StudentMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.QueryHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.StudentRepository;

@Component
public class GetStudentByIdQueryHandler implements QueryHandler<GetStudentByIdQuery, StudentResponse> {

    private final StudentRepository repository;

    public GetStudentByIdQueryHandler(StudentRepository repository) {
        this.repository = repository;
    }

    @Override
    public StudentResponse handle(GetStudentByIdQuery query) {
        return StudentMapper.toDto(repository.findById(query.id())
            .orElseThrow(() -> new IllegalArgumentException("Student not found")));
    }
}
