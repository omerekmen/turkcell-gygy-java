package com.turkcell.library_cqrs.application.features.loan.mapper;

import java.util.List;

import com.turkcell.library_cqrs.application.features.loan.LoanResponse;
import com.turkcell.library_cqrs.domain.entity.Loan;

public final class LoanMapper {

    private LoanMapper() {
    }

    public static LoanResponse toDto(Loan entity) {
        if (entity == null) {
            return null;
        }

        List<java.util.UUID> copyIds = entity.getBookCopies() == null
            ? List.of()
            : entity.getBookCopies().stream().map(c -> c.getId()).toList();

        return new LoanResponse(
            entity.getId(),
            entity.getStudent() != null ? entity.getStudent().getId() : null,
            entity.getStaff() != null ? entity.getStaff().getId() : null,
            entity.getLoanDate(),
            entity.getDueDate(),
            entity.getStatus(),
            copyIds
        );
    }

    public static List<LoanResponse> toDtoList(List<Loan> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream().map(LoanMapper::toDto).toList();
    }
}
