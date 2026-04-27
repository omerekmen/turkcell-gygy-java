package com.turkcell.library_app.dto.loan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.turkcell.library_app.enums.LoanStatus;

public class LoanResponse {
    private UUID id;
    private UUID studentId;
    private UUID staffId;
    private LocalDateTime loanDate;
    private LocalDateTime dueDate;
    private LoanStatus status;
    private List<UUID> bookCopyIds;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public LocalDateTime getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDateTime loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public List<UUID> getBookCopyIds() {
        return bookCopyIds;
    }

    public void setBookCopyIds(List<UUID> bookCopyIds) {
        this.bookCopyIds = bookCopyIds;
    }
}
