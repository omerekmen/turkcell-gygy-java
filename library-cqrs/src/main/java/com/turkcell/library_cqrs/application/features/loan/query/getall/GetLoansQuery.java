package com.turkcell.library_cqrs.application.features.loan.query.getall;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.turkcell.library_cqrs.api.dto.loan.LoanResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Query;
import com.turkcell.library_cqrs.domain.enums.LoanStatus;

public record GetLoansQuery(UUID studentId, LoanStatus status, int page, int size) implements Query<Page<LoanResponse>> {
}
