package com.turkcell.library_cqrs.application.features.loan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.turkcell.library_cqrs.domain.enums.LoanStatus;

public record LoanResponse(
    UUID id,
    UUID studentId,
    UUID staffId,
    LocalDateTime loanDate,
    LocalDateTime dueDate,
    LoanStatus status,
    List<UUID> bookCopyIds
) {}