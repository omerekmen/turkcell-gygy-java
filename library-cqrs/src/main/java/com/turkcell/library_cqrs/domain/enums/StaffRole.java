package com.turkcell.library_cqrs.domain.enums;

public enum StaffRole {
    LIBRARIAN("Librarian"),
    MANAGER("Manager"),
    ASSISTANT("Assistant"),
    CIRCULATION_DESK("Circulation desk staff"),
    SHELF_ORGANIZER("Shelf organizer");

    private final String description;

    StaffRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
