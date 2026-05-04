package com.turkcell.library_cqrs.api.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.library_cqrs.api.dto.ApiResult;
import com.turkcell.library_cqrs.api.dto.reservation.CreateReservationRequest;
import com.turkcell.library_cqrs.api.dto.reservation.ReservationResponse;
import com.turkcell.library_cqrs.api.dto.reservation.UpdateReservationStatusRequest;
import com.turkcell.library_cqrs.application.features.reservation.command.create.CreateReservationCommand;
import com.turkcell.library_cqrs.application.features.reservation.command.delete.DeleteReservationCommand;
import com.turkcell.library_cqrs.application.features.reservation.command.update.UpdateReservationStatusCommand;
import com.turkcell.library_cqrs.application.features.reservation.query.getall.GetReservationsQuery;
import com.turkcell.library_cqrs.application.features.reservation.query.getbyid.GetReservationByIdQuery;
import com.turkcell.library_cqrs.core.mediator.Mediator;
import com.turkcell.library_cqrs.domain.enums.ReservationStatus;

@RestController
@RequestMapping("api/v{version:1}/reservations")
public class ReservationsController {

    private final Mediator mediator;

    public ReservationsController(Mediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping
    public ResponseEntity<ApiResult<ReservationResponse>> create(@RequestBody CreateReservationRequest request) {
        var dto = mediator.send(new CreateReservationCommand(request.getStudentId(), request.getBookId(), request.getExpiresAt()));
        return ResponseEntity.created(URI.create("/reservations/" + dto.getId()))
            .body(ApiResult.success(HttpStatus.CREATED.value(), "Reservation created successfully", dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<ReservationResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResult.success("Reservation retrieved successfully", mediator.send(new GetReservationByIdQuery(id))));
    }

    @GetMapping
    public ResponseEntity<ApiResult<Page<ReservationResponse>>> getAll(
        @RequestParam(required = false) UUID bookId,
        @RequestParam(required = false) ReservationStatus status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(ApiResult.success("Reservations retrieved successfully", mediator.send(new GetReservationsQuery(bookId, status, page, size))));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResult<ReservationResponse>> updateStatus(@PathVariable UUID id, @RequestBody UpdateReservationStatusRequest request) {
        return ResponseEntity.ok(ApiResult.success("Reservation status updated successfully", mediator.send(new UpdateReservationStatusCommand(id, request.getStatus(), request.getExpiresAt()))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> delete(@PathVariable UUID id) {
        mediator.send(new DeleteReservationCommand(id));
        return ResponseEntity.ok(ApiResult.success(HttpStatus.NO_CONTENT.value(), "Reservation deleted successfully"));
    }
}
