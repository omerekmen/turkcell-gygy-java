package com.turkcell.library_cqrs.application.features.reservation.command.update;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.reservation.ReservationResponse;
import com.turkcell.library_cqrs.application.features.reservation.mapper.ReservationMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.ReservationRepository;

@Component
public class UpdateReservationStatusCommandHandler implements CommandHandler<UpdateReservationStatusCommand, ReservationResponse> {

    private final ReservationRepository reservationRepository;

    public UpdateReservationStatusCommandHandler(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public ReservationResponse handle(UpdateReservationStatusCommand command) {
        if (command.status() == null) {
            throw new IllegalArgumentException("status is required");
        }

        var reservation = reservationRepository.findById(command.id())
            .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        reservation.setStatus(command.status());
        if (command.expiresAt() != null) {
            reservation.setExpiresAt(command.expiresAt());
        }

        return ReservationMapper.toDto(reservationRepository.save(reservation));
    }
}
