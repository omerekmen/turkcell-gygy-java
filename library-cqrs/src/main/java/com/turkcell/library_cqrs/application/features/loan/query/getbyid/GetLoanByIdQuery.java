package com.turkcell.library_cqrs.application.features.loan.query.getbyid;

import java.util.UUID;

import com.turkcell.library_cqrs.api.dto.loan.LoanResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Query;

public record GetLoanByIdQuery(UUID id) implements Query<LoanResponse> {
}
