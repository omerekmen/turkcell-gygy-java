package com.turkcell.library_cqrs.application.features.reservation.query.getbyid;

import java.util.UUID;

import com.turkcell.library_cqrs.api.dto.reservation.ReservationResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Query;

public record GetReservationByIdQuery(UUID id) implements Query<ReservationResponse> {
}
