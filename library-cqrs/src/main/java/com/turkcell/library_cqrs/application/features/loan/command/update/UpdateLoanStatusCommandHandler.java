package com.turkcell.library_cqrs.application.features.loan.command.update;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.loan.LoanResponse;
import com.turkcell.library_cqrs.application.features.loan.mapper.LoanMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.LoanRepository;

@Component
public class UpdateLoanStatusCommandHandler implements CommandHandler<UpdateLoanStatusCommand, LoanResponse> {

    private final LoanRepository repository;

    public UpdateLoanStatusCommandHandler(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public LoanResponse handle(UpdateLoanStatusCommand command) {
        var entity = repository.findById(command.id())
            .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        entity.setStatus(command.status());
        return LoanMapper.toDto(repository.save(entity));
    }
}
