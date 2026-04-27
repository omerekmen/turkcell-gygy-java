package com.turkcell.library_app.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.turkcell.library_app.dto.staff.CreateStaffRequest;
import com.turkcell.library_app.dto.staff.StaffResponse;
import com.turkcell.library_app.dto.staff.UpdateStaffRequest;

public interface StaffService {
    StaffResponse create(CreateStaffRequest request);
    StaffResponse getById(UUID id);
    Page<StaffResponse> getAll(int page, int size);
    StaffResponse update(UUID id, UpdateStaffRequest request);
    void delete(UUID id);
}
