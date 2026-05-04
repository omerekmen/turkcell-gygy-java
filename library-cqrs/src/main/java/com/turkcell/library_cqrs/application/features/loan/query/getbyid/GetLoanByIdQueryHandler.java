package com.turkcell.library_cqrs.application.features.loan.query.getbyid;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.loan.LoanResponse;
import com.turkcell.library_cqrs.application.features.loan.mapper.LoanMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.QueryHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.LoanRepository;

@Component
public class GetLoanByIdQueryHandler implements QueryHandler<GetLoanByIdQuery, LoanResponse> {

    private final LoanRepository repository;

    public GetLoanByIdQueryHandler(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public LoanResponse handle(GetLoanByIdQuery query) {
        return LoanMapper.toDto(repository.findById(query.id())
            .orElseThrow(() -> new IllegalArgumentException("Loan not found")));
    }
}
