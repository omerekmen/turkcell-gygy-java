package com.turkcell.library_cqrs.application.features.loan.command.create;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.turkcell.library_cqrs.api.dto.loan.LoanResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

public record CreateLoanCommand(
    UUID studentId,
    UUID staffId,
    LocalDateTime dueDate,
    List<UUID> bookCopyIds
) implements Command<LoanResponse> {
}
