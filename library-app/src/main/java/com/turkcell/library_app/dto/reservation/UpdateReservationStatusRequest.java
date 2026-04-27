package com.turkcell.library_app.dto.reservation;

import java.time.LocalDateTime;

import com.turkcell.library_app.enums.ReservationStatus;

public class UpdateReservationStatusRequest {
    private ReservationStatus status;
    private LocalDateTime expiresAt;

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
