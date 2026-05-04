package com.turkcell.library_cqrs.application.features.loan.command.delete;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.LoanRepository;

@Component
public class DeleteLoanCommandHandler implements CommandHandler<DeleteLoanCommand, Void> {

    private final LoanRepository repository;

    public DeleteLoanCommandHandler(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Void handle(DeleteLoanCommand command) {
        if (!repository.existsById(command.id())) {
            throw new IllegalArgumentException("Loan not found");
        }

        repository.deleteById(command.id());
        return null;
    }
}
