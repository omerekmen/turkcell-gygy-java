package com.turkcell.library_app.dto.loan;

import com.turkcell.library_app.enums.LoanStatus;

public class UpdateLoanStatusRequest {
    private LoanStatus status;

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }
}
