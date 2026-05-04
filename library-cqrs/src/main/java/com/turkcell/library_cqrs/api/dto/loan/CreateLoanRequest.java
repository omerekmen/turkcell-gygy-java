package com.turkcell.library_cqrs.api.dto.loan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CreateLoanRequest {
    private UUID studentId;
    private UUID staffId;
    private LocalDateTime dueDate;
    private List<UUID> bookCopyIds;

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public UUID getStaffId() {
        return staffId;
    }

    public void setStaffId(UUID staffId) {
        this.staffId = staffId;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public List<UUID> getBookCopyIds() {
        return bookCopyIds;
    }

    public void setBookCopyIds(List<UUID> bookCopyIds) {
        this.bookCopyIds = bookCopyIds;
    }
}
