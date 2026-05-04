package com.turkcell.library_cqrs.application.features.reservation.query.getall;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.turkcell.library_cqrs.api.dto.reservation.ReservationResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Query;
import com.turkcell.library_cqrs.domain.enums.ReservationStatus;

public record GetReservationsQuery(UUID bookId, ReservationStatus status, int page, int size) implements Query<Page<ReservationResponse>> {
}
