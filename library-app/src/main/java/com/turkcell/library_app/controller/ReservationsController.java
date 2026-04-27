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

@RestController
@RequestMapping("/api/v{version:1}/reservations")
public class ReservationsController {
    private final ReservationService reservationService;

    public ReservationsController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ReservationResponse create(@RequestBody CreateReservationRequest request) {
        return reservationService.create(request);
    }

    @GetMapping("/{id}")
    public ReservationResponse getById(@PathVariable UUID id) {
        return reservationService.getById(id);
    }

    @GetMapping
    public Page<ReservationResponse> getAll(
        @RequestParam(required = false) UUID bookId,
        @RequestParam(required = false) ReservationStatus status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return reservationService.getAll(bookId, status, page, size);
    }

    @PatchMapping("/{id}/status")
    public ReservationResponse updateStatus(@PathVariable UUID id, @RequestBody UpdateReservationStatusRequest request) {
        return reservationService.updateStatus(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        reservationService.delete(id);
    }
}
