package com.turkcell.library_cqrs.domain.enums;

public enum LoanStatus {
    ACTIVE("Loan is currently active"),
    COMPLETED("Loan has been completed"),
    OVERDUE("Loan is overdue");

    private final String description;

    LoanStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
