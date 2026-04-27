package com.turkcell.library_app.dto.staff;

import com.turkcell.library_app.enums.StaffRole;

public class UpdateStaffRequest {
    private String staffNumber;
    private StaffRole role;
    private Boolean isActive;

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
