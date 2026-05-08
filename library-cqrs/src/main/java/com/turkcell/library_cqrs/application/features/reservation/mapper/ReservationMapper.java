package com.turkcell.library_cqrs.application.features.reservation.mapper;

import java.util.List;

import com.turkcell.library_cqrs.application.features.reservation.ReservationResponse;
import com.turkcell.library_cqrs.domain.entity.Reservation;

public final class ReservationMapper {

    private ReservationMapper() {
    }

    public static ReservationResponse toDto(Reservation entity) {
        if (entity == null) {
            return null;
        }

        return new ReservationResponse(
            entity.getId(),
            entity.getStudent() != null ? entity.getStudent().getId() : null,
            entity.getBook() != null ? entity.getBook().getId() : null,
            entity.getStatus(),
            entity.getPosition(),
            entity.getReservedAt(),
            entity.getExpiresAt()
        );
    }

    public static List<ReservationResponse> toDtoList(List<Reservation> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream().map(ReservationMapper::toDto).toList();
    }
}
