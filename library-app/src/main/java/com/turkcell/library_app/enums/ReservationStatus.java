package com.turkcell.library_app.enums;

public enum ReservationStatus {
    PENDING("Reservation is pending"),
    READY("Book copy is ready for pickup"),
    EXPIRED("Reservation has expired"),
    CANCELLED("Reservation has been cancelled");

    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
