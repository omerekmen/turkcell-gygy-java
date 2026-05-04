package com.turkcell.library_cqrs.domain.enums;

public enum BookCopyStatus {
    AVAILABLE("Available for lending"),
    BORROWED("Currently borrowed by a student"),
    RESERVED("Reserved by a student"),
    DAMAGED("Damaged and unavailable"),
    LOST("Lost or missing");

    private final String description;

    BookCopyStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
