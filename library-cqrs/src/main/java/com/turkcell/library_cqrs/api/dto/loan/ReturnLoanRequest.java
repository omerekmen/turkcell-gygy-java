package com.turkcell.library_cqrs.api.dto.loan;

import java.util.UUID;

public class ReturnLoanRequest {
    private UUID processedByStaffId;

    public UUID getProcessedByStaffId() {
        return processedByStaffId;
    }

    public void setProcessedByStaffId(UUID processedByStaffId) {
        this.processedByStaffId = processedByStaffId;
    }
}
