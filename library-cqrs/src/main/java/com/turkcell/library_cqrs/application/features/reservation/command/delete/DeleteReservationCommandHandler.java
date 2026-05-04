package com.turkcell.library_cqrs.application.features.reservation.command.delete;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.ReservationRepository;

@Component
public class DeleteReservationCommandHandler implements CommandHandler<DeleteReservationCommand, Void> {

    private final ReservationRepository reservationRepository;

    public DeleteReservationCommandHandler(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Void handle(DeleteReservationCommand command) {
        var reservation = reservationRepository.findById(command.id())
            .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        reservationRepository.delete(reservation);
        return null;
    }
}
