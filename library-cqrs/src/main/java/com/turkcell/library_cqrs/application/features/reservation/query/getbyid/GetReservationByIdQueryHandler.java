package com.turkcell.library_cqrs.application.features.reservation.query.getbyid;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.reservation.ReservationResponse;
import com.turkcell.library_cqrs.application.features.reservation.mapper.ReservationMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.QueryHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.ReservationRepository;

@Component
public class GetReservationByIdQueryHandler implements QueryHandler<GetReservationByIdQuery, ReservationResponse> {

    private final ReservationRepository repository;

    public GetReservationByIdQueryHandler(ReservationRepository repository) {
        this.repository = repository;
    }

    @Override
    public ReservationResponse handle(GetReservationByIdQuery query) {
        return ReservationMapper.toDto(repository.findById(query.id())
            .orElseThrow(() -> new IllegalArgumentException("Reservation not found")));
    }
}
