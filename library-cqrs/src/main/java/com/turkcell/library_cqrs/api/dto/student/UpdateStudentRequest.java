package com.turkcell.library_cqrs.api.dto.student;

public class UpdateStudentRequest {
    private String studentNumber;
    private Boolean isActive;

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
