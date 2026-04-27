package com.turkcell.library_app.dto.loan;

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
