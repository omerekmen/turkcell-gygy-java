package com.turkcell.library_cqrs.application.features.student.query.getall;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.student.StudentResponse;
import com.turkcell.library_cqrs.application.features.student.mapper.StudentMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.QueryHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.StudentRepository;

@Component
public class GetStudentsQueryHandler implements QueryHandler<GetStudentsQuery, Page<StudentResponse>> {

    private final StudentRepository repository;

    public GetStudentsQueryHandler(StudentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<StudentResponse> handle(GetStudentsQuery query) {
        return repository.findAll(PageRequest.of(query.page(), query.size())).map(StudentMapper::toDto);
    }
}
