package com.turkcell.library_cqrs.api.dto.reservation;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreateReservationRequest {
    private UUID studentId;
    private UUID bookId;
    private LocalDateTime expiresAt;

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
