package com.turkcell.library_cqrs.application.features.loan.mapper;

import java.util.List;

import com.turkcell.library_cqrs.api.dto.loan.LoanResponse;
import com.turkcell.library_cqrs.domain.entity.Loan;

public final class LoanMapper {

    private LoanMapper() {
    }

    public static LoanResponse toDto(Loan entity) {
        if (entity == null) {
            return null;
        }

        var dto = new LoanResponse();
        dto.setId(entity.getId());
        dto.setStudentId(entity.getStudent() != null ? entity.getStudent().getId() : null);
        dto.setStaffId(entity.getStaff() != null ? entity.getStaff().getId() : null);
        dto.setLoanDate(entity.getLoanDate());
        dto.setDueDate(entity.getDueDate());
        dto.setStatus(entity.getStatus());

        List<java.util.UUID> copyIds = entity.getBookCopies() == null
            ? List.of()
            : entity.getBookCopies().stream().map(c -> c.getId()).toList();
        dto.setBookCopyIds(copyIds);

        return dto;
    }

    public static List<LoanResponse> toDtoList(List<Loan> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream().map(LoanMapper::toDto).toList();
    }
}
