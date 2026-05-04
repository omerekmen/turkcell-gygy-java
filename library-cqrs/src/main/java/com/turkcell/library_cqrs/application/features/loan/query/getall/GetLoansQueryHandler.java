package com.turkcell.library_cqrs.application.features.loan.query.getall;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.loan.LoanResponse;
import com.turkcell.library_cqrs.application.features.loan.mapper.LoanMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.QueryHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.LoanRepository;

@Component
public class GetLoansQueryHandler implements QueryHandler<GetLoansQuery, Page<LoanResponse>> {

    private final LoanRepository repository;

    public GetLoansQueryHandler(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<LoanResponse> handle(GetLoansQuery query) {
        var pageable = PageRequest.of(query.page(), query.size());

        if (query.studentId() != null && query.status() != null) {
            return repository.findByStudentIdAndStatus(query.studentId(), query.status(), pageable).map(LoanMapper::toDto);
        }

        if (query.studentId() != null) {
            return repository.findByStudentId(query.studentId(), pageable).map(LoanMapper::toDto);
        }

        if (query.status() != null) {
            return repository.findByStatus(query.status(), pageable).map(LoanMapper::toDto);
        }

        return repository.findAll(pageable).map(LoanMapper::toDto);
    }
}
