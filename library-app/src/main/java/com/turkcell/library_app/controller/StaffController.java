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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.library_app.dto.ApiResult;

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
    public ApiResult<StaffResponse> create(@RequestBody CreateStaffRequest request) {
        StaffResponse response = staffService.create(request);
        return ApiResult.success(HttpStatus.CREATED.value(), "Staff created successfully", response);
    }

    @GetMapping("/{id}")
    public ApiResult<StaffResponse> getById(@PathVariable UUID id) {
        StaffResponse response = staffService.getById(id);
        return ApiResult.success("Staff retrieved successfully", response);
    }

    @GetMapping
    public ApiResult<Page<StaffResponse>> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        Page<StaffResponse> response = staffService.getAll(page, size);
        return ApiResult.success("Staff retrieved successfully", response);
    }

    @PutMapping("/{id}")
    public ApiResult<StaffResponse> update(@PathVariable UUID id, @RequestBody UpdateStaffRequest request) {
        StaffResponse response = staffService.update(id, request);
        return ApiResult.success("Staff updated successfully", response);
        
    }
    
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable UUID id) {
        staffService.delete(id);
        return ApiResult.success(HttpStatus.NO_CONTENT.value(), "Staff deleted successfully");
    }
}
        