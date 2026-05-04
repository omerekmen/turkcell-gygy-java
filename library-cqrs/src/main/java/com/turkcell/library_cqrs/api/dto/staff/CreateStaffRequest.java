package com.turkcell.library_cqrs.api.dto.staff;

import java.util.UUID;

import com.turkcell.library_cqrs.domain.enums.StaffRole;

public class CreateStaffRequest {
    private UUID userId;
    private String staffNumber;
    private StaffRole role;
    private Boolean isActive;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getStaffNumber() {
        return staffNumber;
    }

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    public StaffRole getRole() {
        return role;
    }

    public void setRole(StaffRole role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
