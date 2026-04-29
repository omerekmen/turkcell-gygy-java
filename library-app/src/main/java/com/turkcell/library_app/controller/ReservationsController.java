package com.turkcell.library_app.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.library_app.dto.reservation.CreateReservationRequest;
import com.turkcell.library_app.dto.reservation.ReservationResponse;
import com.turkcell.library_app.dto.reservation.UpdateReservationStatusRequest;
import com.turkcell.library_app.enums.ReservationStatus;
import com.turkcell.library_app.service.ReservationService;

import org.springframework.http.HttpStatus;
import com.turkcell.library_app.dto.ApiResult;

@RestController
@RequestMapping("/api/v{version:1}/reservations")
public class ReservationsController {
    private final ReservationService reservationService;

    public ReservationsController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


    @PostMapping
    public ApiResult<ReservationResponse> create(@RequestBody CreateReservationRequest request) {
        ReservationResponse response = reservationService.create(request);
        return ApiResult.success(HttpStatus.CREATED.value(), "Reservation created successfully", response);
    }

    @GetMapping("/{id}")
    public ApiResult<ReservationResponse> getById(@PathVariable UUID id) {
        ReservationResponse response = reservationService.getById(id);
        return ApiResult.success("Reservation retrieved successfully", response);
    }

    @GetMapping
    public ApiResult<Page<ReservationResponse>> getAll(
        @RequestParam(required = false) UUID bookId,
        @RequestParam(required = false) ReservationStatus status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        Page<ReservationResponse> response = reservationService.getAll(bookId, status, page, size);
        return ApiResult.success("Reservations retrieved successfully", response);
    }

    @PatchMapping("/{id}/status")
    public ApiResult<ReservationResponse> updateStatus(@PathVariable UUID id, @RequestBody UpdateReservationStatusRequest request) {
        ReservationResponse response = reservationService.updateStatus(id, request);
        return ApiResult.success("Reservation status updated successfully", response);
    }
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable UUID id) {
        reservationService.delete(id);
        return ApiResult.success(HttpStatus.NO_CONTENT.value(), "Reservation deleted successfully");
    }
}
