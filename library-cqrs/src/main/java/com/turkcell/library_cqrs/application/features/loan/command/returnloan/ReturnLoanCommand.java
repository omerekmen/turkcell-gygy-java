package com.turkcell.library_cqrs.application.features.loan.command.returnloan;

import java.util.UUID;

import com.turkcell.library_cqrs.api.dto.loan.LoanResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

public record ReturnLoanCommand(UUID loanId, UUID processedByStaffId) implements Command<LoanResponse> {
}
