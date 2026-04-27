package com.turkcell.library_app.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.library_app.dto.staff.CreateStaffRequest;
import com.turkcell.library_app.dto.staff.StaffResponse;
import com.turkcell.library_app.dto.staff.UpdateStaffRequest;
import com.turkcell.library_app.service.StaffService;

@RestController
@RequestMapping("/api/v{version:1}/staff")
public class StaffController {
    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping
    public StaffResponse create(@RequestBody CreateStaffRequest request) {
        return staffService.create(request);
    }

    @GetMapping("/{id}")
    public StaffResponse getById(@PathVariable UUID id) {
        return staffService.getById(id);
    }

    @GetMapping
    public Page<StaffResponse> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return staffService.getAll(page, size);
    }

    @PutMapping("/{id}")
    public StaffResponse update(@PathVariable UUID id, @RequestBody UpdateStaffRequest request) {
        return staffService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        staffService.delete(id);
    }
}
