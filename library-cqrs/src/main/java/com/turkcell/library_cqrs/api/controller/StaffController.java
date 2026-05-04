package com.turkcell.library_cqrs.api.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.library_cqrs.api.dto.ApiResult;
import com.turkcell.library_cqrs.api.dto.staff.CreateStaffRequest;
import com.turkcell.library_cqrs.api.dto.staff.StaffResponse;
import com.turkcell.library_cqrs.api.dto.staff.UpdateStaffRequest;
import com.turkcell.library_cqrs.application.features.staff.command.create.CreateStaffCommand;
import com.turkcell.library_cqrs.application.features.staff.command.delete.DeleteStaffCommand;
import com.turkcell.library_cqrs.application.features.staff.command.update.UpdateStaffCommand;
import com.turkcell.library_cqrs.application.features.staff.query.getall.GetStaffsQuery;
import com.turkcell.library_cqrs.application.features.staff.query.getbyid.GetStaffByIdQuery;
import com.turkcell.library_cqrs.core.mediator.Mediator;

@RestController
@RequestMapping("api/v{version:1}/staff")
public class StaffController {

    private final Mediator mediator;

    public StaffController(Mediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping
    public ResponseEntity<ApiResult<StaffResponse>> create(@RequestBody CreateStaffRequest request) {
        var dto = mediator.send(new CreateStaffCommand(request.getUserId(), request.getStaffNumber(), request.getRole(), request.getIsActive()));
        return ResponseEntity.created(URI.create("/staff/" + dto.getId()))
            .body(ApiResult.success(HttpStatus.CREATED.value(), "Staff created successfully", dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<StaffResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResult.success("Staff retrieved successfully", mediator.send(new GetStaffByIdQuery(id))));
    }

    @GetMapping
    public ResponseEntity<ApiResult<Page<StaffResponse>>> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(ApiResult.success("Staff retrieved successfully", mediator.send(new GetStaffsQuery(page, size))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<StaffResponse>> update(@PathVariable UUID id, @RequestBody UpdateStaffRequest request) {
        return ResponseEntity.ok(ApiResult.success("Staff updated successfully", mediator.send(new UpdateStaffCommand(id, request.getStaffNumber(), request.getRole(), request.getIsActive()))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> delete(@PathVariable UUID id) {
        mediator.send(new DeleteStaffCommand(id));
        return ResponseEntity.ok(ApiResult.success(HttpStatus.NO_CONTENT.value(), "Staff deleted successfully"));
    }
}
