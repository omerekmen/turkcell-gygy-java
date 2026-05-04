package com.turkcell.library_cqrs.application.features.reservation.mapper;

import java.util.List;

import com.turkcell.library_cqrs.api.dto.reservation.ReservationResponse;
import com.turkcell.library_cqrs.domain.entity.Reservation;

public final class ReservationMapper {

    private ReservationMapper() {
    }

    public static ReservationResponse toDto(Reservation entity) {
        if (entity == null) {
            return null;
        }

        var dto = new ReservationResponse();
        dto.setId(entity.getId());
        dto.setStudentId(entity.getStudent() != null ? entity.getStudent().getId() : null);
        dto.setBookId(entity.getBook() != null ? entity.getBook().getId() : null);
        dto.setStatus(entity.getStatus());
        dto.setPosition(entity.getPosition());
        dto.setReservedAt(entity.getReservedAt());
        dto.setExpiresAt(entity.getExpiresAt());
        return dto;
    }

    public static List<ReservationResponse> toDtoList(List<Reservation> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream().map(ReservationMapper::toDto).toList();
    }
}
