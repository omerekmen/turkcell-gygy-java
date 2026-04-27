package com.turkcell.library_app.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.turkcell.library_app.dto.reservation.CreateReservationRequest;
import com.turkcell.library_app.dto.reservation.ReservationResponse;
import com.turkcell.library_app.dto.reservation.UpdateReservationStatusRequest;
import com.turkcell.library_app.enums.ReservationStatus;

public interface ReservationService {
    ReservationResponse create(CreateReservationRequest request);
    ReservationResponse getById(UUID id);
    Page<ReservationResponse> getAll(UUID bookId, ReservationStatus status, int page, int size);
    ReservationResponse updateStatus(UUID id, UpdateReservationStatusRequest request);
    void delete(UUID id);
}
