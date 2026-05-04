package com.turkcell.library_cqrs.application.features.loan.command.update;

import java.util.UUID;

import com.turkcell.library_cqrs.api.dto.loan.LoanResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Command;
import com.turkcell.library_cqrs.domain.enums.LoanStatus;

public record UpdateLoanStatusCommand(UUID id, LoanStatus status) implements Command<LoanResponse> {
}
